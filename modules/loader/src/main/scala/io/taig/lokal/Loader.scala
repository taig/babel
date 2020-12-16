package io.taig.lokal

import cats.effect.{Blocker, ContextShift, MonadThrow, Sync}
import cats.syntax.all._
import fs2.Stream
import fs2.io.file.readAll
import fs2.text.utf8Decode
import java.nio.file.{Files, Paths, Path => JPath}

import scala.jdk.CollectionConverters._
import scala.util.control.NoStackTrace

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

  /** Turn all `Some(Locale) -> Dictionaries` into `I18n`s with the `None -> Dictionaries` as fallbacks */
  private def toI18n(values: Map[Option[Locale], Dictionary]): Either[String, I18n] = {
    val fallbacks = values.getOrElse(None, Dictionary.Empty).toI18nUniversals

    values
      .collect { case (Some(locale), dictionary) => dictionary.toI18n(locale) }
      .foldLeft(fallbacks.asRight[String]) {
        case (Right(result), i18n) => result.merge(i18n)
        case (left @ Left(_), _)   => left
      }
  }

  def auto[F[_]: Sync: ContextShift](
      blocker: Blocker,
      resource: String = "i18n",
      loader: ClassLoader = getClass.getClassLoader
  )(
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
            val missingTranslations = locales
              .map(locale => locale -> i18n.missingTranslations(locale))
              .filter(_._2.nonEmpty)

            if (missingTranslations.isEmpty) Right(i18n)
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
