package net.slozzer.babel

import cats.effect.{Blocker, Concurrent, ContextShift, Resource}

import java.nio.file.{Path => JPath}

abstract class Loader[F[_]] {
  def scan(base: String): F[Set[JPath]]

  def filter(paths: Set[JPath], name: String): F[Map[Option[Locale], JPath]]
}

object Loader {
  val Wildcard = "*"

  def default[F[_]: Concurrent: ContextShift](blocker: Blocker): Resource[F, Loader[F]] = ClassgraphLoader[F](blocker)
}
