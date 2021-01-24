package net.slozzer.babel

sealed abstract class Babel extends Product with Serializable

object Babel {
  final case class Object(values: Map[String, Babel]) extends Babel
  final case class Value(value: String) extends Babel
}
