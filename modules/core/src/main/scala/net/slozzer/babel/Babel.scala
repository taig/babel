package net.slozzer.babel

sealed abstract class Babel extends Product with Serializable {
  final def get(key: String): Option[Babel] = this match {
    case Babel.Object(values) => values.get(key)
    case Babel.Value(_)       => None
    case Babel.Null           => None
  }
}

object Babel {
  final case object Null extends Babel
  final case class Object(values: Map[String, Babel]) extends Babel
  final case class Value(value: String) extends Babel

  val Empty: Babel = Object(Map.empty)

  def from(values: Iterable[(String, Babel)]): Babel = Object(values.toMap)

  def of(values: (String, Babel)*): Babel = from(values)

  def one(key: String, value: Babel): Babel = Object(Map(key -> value))
}
