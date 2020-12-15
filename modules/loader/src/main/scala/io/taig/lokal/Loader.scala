package io.taig.lokal

import cats.effect.{Blocker, ContextShift, MonadThrow, Sync}
import cats.syntax.all._
import fs2.Stream
import fs2.io.file.readAll
import fs2.text.utf8Decode

import java.nio.file.{Files, Paths, Path => JPath}
import scala.jdk.CollectionConverters._

object Loader {

  /** List all files that represent a locale (e.g. de, en-US) or the '*' wildcard */
  private def list[F[_]: Sync: ContextShift](
      blocker: Blocker,
      path: JPath,
      extension: String
  ): Stream[F, (Option[Locale], JPath)] =
    Stream
      .fromBlockingIterator[F](blocker, Files.list(path).iterator().asScala)
      .evalFilter(path => blocker.delay(Files.isRegularFile(path)))
      .mapFilter { path =>
        val fullName = path.getFileName.toString
        val suffix = s".$extension"

        if (fullName.endsWith(suffix)) {
          val name = fullName.replace(suffix, "")
          if (name == "*") Some((None, path)) else Locale.parseLanguageTag(name).map(locale => (Some(locale), path))
        } else None
      }

  private def read[F[_]: Sync: ContextShift](blocker: Blocker, path: JPath): F[String] =
    readAll(path, blocker, chunkSize = 8192)
      .through(utf8Decode[F])
      .compile
      .string

  private def parse[F[_], A](value: String)(implicit F: MonadThrow[F], parser: Parser[A]): F[A] =
    F.fromEither(parser.parse(value).leftMap(reason => new RuntimeException(s"Parsing failure: $reason")))

  private def toI18n(values: Map[Option[Locale], Dictionary]): Either[String, I18n] = {
    val fallbacks = values.getOrElse(None, Dictionary.Empty).toI18nFallbacks

    values
      .collect { case (Some(locale), dictionary) => dictionary.toI18n(locale) }
      .foldLeft(fallbacks.asRight[String]) {
        case (Right(result), i18n) => result.merge(i18n)
        case (left @ Left(_), _)   => left
      }
  }

  def auto[F[_]: Sync: ContextShift](blocker: Blocker, resource: String, loader: ClassLoader)(
      implicit parser: Parser[Dictionary]
  ): F[I18n] = {
    val path = blocker.delay {
      val url = loader.getResource(resource)
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
      .map(_.toMap)
      .map { dictionaries =>
        val locales = dictionaries.keySet.collect { case Some(locale) => locale }

        toI18n(dictionaries)
          .leftMap(new RuntimeException(_))
          .flatMap { i18n =>
            locales
              .collectFirst {
                case locale if !i18n.supports(locale) =>
                  new IllegalArgumentException(s"Incomplete support: ${locale.printLanguageTag}")
              }
              .toLeft(i18n)
          }
      }
      .rethrow
  }
}
