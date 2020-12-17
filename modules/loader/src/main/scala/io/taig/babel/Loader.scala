package io.taig.babel

import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.{FileSystems, Path => JPath}
import java.util.Collections

import scala.jdk.CollectionConverters._
import scala.util.control.NoStackTrace

import cats.effect.syntax.all._
import cats.effect.{Blocker, ConcurrentEffect, ContextShift, IO, MonadThrow, Resource}
import cats.syntax.all._
import fs2.Stream
import fs2.concurrent.Queue
import io.github.classgraph.ClassGraph

object Loader {

  /** List all files that represent a locale (e.g. de, en-US) or the '*' wildcard */
  private def list[F[_]: ContextShift](
      blocker: Blocker,
      resource: String,
      extension: String
  )(implicit F: ConcurrentEffect[F]): Stream[F, (Option[Locale], String)] = {
    Stream.eval(Queue.noneTerminated[F, (Option[Locale], String)]).flatMap { queue =>
      val scan = F.delay(new ClassGraph().acceptPathsNonRecursive(resource).scan())

      val reader = Resource
        .fromAutoCloseableBlocking(blocker)(scan)
        .map(_.getResourcesWithExtension(extension))
        .flatTap { resources =>
          resources.asScala.toList
            .map(_.getURI.toString.split('!'))
            .mapFilter {
              case Array(jar, _) => Some(jar)
              case _             => None
            }
            .distinct
            .traverse { path =>
              Resource.fromAutoCloseableBlocking(blocker)(
                F.delay(FileSystems.newFileSystem(URI.create(path), Collections.emptyMap[String, AnyRef]()))
              )
            }
        }
        .onFinalize(queue.enqueue1(None))
        .use { resources =>
          blocker.delay {
            resources.forEachByteArrayThrowingIOException { (resource, data) =>
              val name = JPath
                .of(resource.getURI)
                .getFileName
                .toString
                .replaceFirst(s".$extension$$", "")

              val content = new String(data, StandardCharsets.UTF_8)

              if (name == "*") queue.enqueue1(Some(None -> content)).runAsync(_ => IO.unit).unsafeRunSync()
              else
                Locale.parseLanguageTag(name).foreach { locale =>
                  queue.enqueue1(Some(Some(locale) -> content)).runAsync(_ => IO.unit).unsafeRunSync()
                }
            }
          }
        }
        .handleErrorWith(throwable => F.delay(throwable.printStackTrace()))

      Stream.resource(reader.background) *> queue.dequeue
    }
  }

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

  def auto[F[_]: ConcurrentEffect: ContextShift](
      blocker: Blocker,
      resource: String = "babel",
      extension: String = "json"
  )(
      implicit parser: Parser[Dictionary]
  ): F[Babel] = {
    list(blocker, resource, extension)
      .evalMap { case (locale, content) => parse[F, Dictionary](content).tupleLeft(locale) }
      .compile
      .toList
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
