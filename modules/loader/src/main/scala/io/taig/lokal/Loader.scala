package io.taig.lokal

import cats.effect.{Blocker, ContextShift, ExitCode, IO, IOApp, Sync}
import cats.effect.MonadThrow
import cats.syntax.all._
import fs2.io.readInputStream
import fs2.text.utf8Decode
import io.taig.lokal.Loader.auto

import java.io.InputStream
import java.nio.file.{FileSystems, Files, Paths}
import java.nio.file.{Path => JPath}
import java.util.Collections
import scala.jdk.CollectionConverters._
import fs2.Stream
import fs2.io.file.readAll

object Loader {

  /** List all files that represent a locale (e.g. de, en-US) or the '*' wildcard */
  private def list[F[_]: Sync: ContextShift](
      blocker: Blocker,
      path: JPath,
      extension: String
  ): Stream[F, (Option[Locale], JPath)] = {
    val needle = s".$extension"

    Stream
      .fromBlockingIterator[F](blocker, Files.list(path).iterator().asScala)
      .evalFilter(path => blocker.delay(path.endsWith(needle) && Files.isRegularFile(path)))
      .mapFilter { path =>
        val name = path.getFileName.toString.replace(needle, "")
        if (name == "*") Some((None, path))
        else Locale.parseLanguageTag(name).map(locale => (Some(locale), path))
      }
  }

  private def read[F[_]: Sync: ContextShift](blocker: Blocker, path: JPath): F[String] =
    readAll(path, blocker, chunkSize = 8192)
      .through(utf8Decode[F])
      .compile
      .string

  private def parse[F[_], A](value: String)(implicit F: MonadThrow[F], parser: Parser[A]): F[A] =
    F.fromEither(parser.parse(value).leftMap(reason => new RuntimeException(s"Parsing failure: $reason")))

  private def toI18n(values: Map[Option[Locale], Dictionary]): I18n = {
    val fallbacks = values.getOrElse(None, Dictionary.Empty)
    values
      .collect { case (Some(locale), dictionary) => (locale, dictionary) }
      .map { case (locale, dictionary) => dictionary.toI18n(locale) }
    ???
  }

  def auto[F[_]: Sync: ContextShift](blocker: Blocker, resource: String)(implicit parser: Parser[Dictionary]) = {
    val path = blocker.delay {
      val url = getClass.getClassLoader.getResource(resource)
      FileSystems.newFileSystem(url.toURI, Collections.emptyMap[String, AnyRef]())
      Option(url).map(url => Paths.get(url.toURI))
    }

    Stream
      .eval(path)
      .evalMap(_.liftTo[F](new IllegalArgumentException(s"Resource not found: $resource")))
      .flatMap(list(blocker, _, "json"))
      .compile
      .toList
      .flatMap { paths =>
        paths.traverse {
          case (locale, path) =>
            read(blocker, path).flatMap(parse[F, Dictionary]).tupleLeft(locale)
        }
      }
  }

  final case class Yolo[A](foo: A, bar: A)

  val x: Yolo[Translation] = ???
  val y: Map[Locale, Map[Path, Text]] = ???
  val z: Map[Locale, Yolo[Text]] = ???
  z.apply(???).foo()
  x.foo
}
