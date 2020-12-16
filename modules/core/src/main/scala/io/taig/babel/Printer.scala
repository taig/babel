package io.taig.babel

import simulacrum.typeclass

@typeclass
trait Printer[A] {
  def print(value: A): String

  final def contramap[B](f: B => A): Printer[B] = value => print(f(value))
}

object Printer {
  implicit val string: Printer[String] = identity

  implicit val int: Printer[Int] = _.toString
}
