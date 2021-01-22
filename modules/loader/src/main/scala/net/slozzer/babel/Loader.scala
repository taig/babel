package net.slozzer.babel

import cats.effect.{Blocker, Concurrent, ContextShift, Resource}

import java.nio.file.{Path => JPath}

abstract class Loader[F[_]] {
  def scan(name: String): F[Set[JPath]]

  def filter(paths: Set[JPath], name: String): Map[Option[Locale], JPath]
}

object Loader {
  def default[F[_]: Concurrent: ContextShift](blocker: Blocker): Resource[F, Loader[F]] = ClassgraphLoader[F](blocker)
}
