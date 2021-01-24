package net.slozzer.babel

import cats.effect.{Blocker, Concurrent, ContextShift, MonadThrow, Resource}
import cats.syntax.all._
import java.nio.file.{Path => JPath}

abstract class Loader[F[_]] {
  def scan(base: String): F[Set[JPath]]

  final def filter(paths: Set[JPath], rule: PathFilter): Map[Option[Locale], JPath] = Loader.filter(paths, rule)

  def load(paths: Map[Option[Locale], JPath]): F[Map[Option[Locale], Array[Byte]]]

  final def parse[A: Parser](paths: Map[Option[Locale], JPath])(implicit F: MonadThrow[F]): F[Map[Option[Locale], A]] =
    load(paths)
      .map(_.toList.traverse { case (locale, bytes) => Parser[A].parse(bytes).tupleLeft(locale) }.map(_.toMap))
      .rethrow
}

object Loader {
  val Wildcard = "*"

  def default[F[_]: Concurrent: ContextShift](blocker: Blocker): Resource[F, Loader[F]] = ClassgraphLoader[F](blocker)

  def filter(paths: Set[JPath], rule: PathFilter): Map[Option[Locale], JPath] =
    paths.toList
      .filter(rule.matches)
      .mapFilter { path =>
        Option(path.getFileName).map(_.toString).map { fileName =>
          val name = fileName.indexOf('.') match {
            case -1    => fileName
            case index => fileName.substring(0, index)
          }

          val locale = name match {
            case Loader.Wildcard => None
            case name            => Locale.parseLanguageTag(name)
          }

          locale -> path
        }
      }
      .toMap
}
