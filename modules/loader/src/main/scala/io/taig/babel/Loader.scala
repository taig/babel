package io.taig.babel

import java.nio.file.{FileSystems, Files, Path => JPath}
import java.util.Collections

import scala.jdk.CollectionConverters._
import scala.util.control.NoStackTrace

import cats.effect.{Blocker, ContextShift, MonadThrow, Resource, Sync}
import cats.syntax.all._
import fs2.Stream
import fs2.io.file.readAll
import fs2.text.utf8Decode

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

  /** Turn all `Some(Locale) -> Dictionaries` into `Babel`s with the `None -> Dictionaries` as universal fallbacks */
  private def toBabel(values: Map[Option[Locale], Dictionary]): Either[String, Babel] = {
    val fallbacks = values.getOrElse(None, Dictionary.Empty).toBabelUniversals

    values
      .collect { case (Some(locale), dictionary) => dictionary.toBabel(locale) }
      .foldLeft(fallbacks.asRight[String]) {
        case (Right(result), babel) => result.merge(babel)
        case (left @ Left(_), _)    => left
      }
  }

  def auto[F[_]: ContextShift](
      blocker: Blocker,
      resource: String = "babel",
      loader: ClassLoader = getClass.getClassLoader
  )(
      implicit
      F: Sync[F],
      parser: Parser[Dictionary]
  ): F[Babel] = {
    val path = Resource
      .liftF(blocker.delay(loader.getResource(resource)))
      .flatMap { url =>
        Option(url).map(_.toURI).traverse { uri =>
          if (uri.getScheme == "jar") {
            Resource
              .fromAutoCloseableBlocking(blocker)(
                F.delay(FileSystems.newFileSystem(uri, Collections.emptyMap(), loader))
              )
              .map(_.getPath(resource))
          } else Resource.liftF(blocker.delay(JPath.of(url.toURI)))
        }
      }
      .evalMap(_.liftTo[F](new IllegalArgumentException(s"Resource not found: $resource")))

    Stream
      .resource(path)
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

        toBabel(dictionaries)
          .leftMap(new RuntimeException(_))
          .flatMap { babel =>
            val missingTranslations = locales
              .map(locale => locale -> babel.missingTranslations(locale))
              .filter(_._2.nonEmpty)

            if (missingTranslations.isEmpty) Right(babel)
            else {
              val details = missingTranslations
                .map {
                  case (locale, paths) =>
                    locale.printLanguageTag + ":\n" + paths.map(path => s" - ${path.printPretty}").mkString("\n")
                }
                .mkString("\n")

              Left(new RuntimeException(s"Missing translations\n$details") with NoStackTrace)
            }
          }
      }
      .rethrow
  }
}
