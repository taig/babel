package net.slozzer.babel

import simulacrum.typeclass

@typeclass
trait Format[A] {
  def format(value: A): String
}

object Format {
  def fromToString[A]: Format[A] = _.toString

  implicit val string: Format[String] = identity

  implicit val int: Format[Int] = fromToString
}
