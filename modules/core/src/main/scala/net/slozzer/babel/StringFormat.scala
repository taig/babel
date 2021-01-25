package net.slozzer.babel

import simulacrum.typeclass

@typeclass
trait Format[A] {
  def format(value: A): String
}

object Format {
  implicit val string: Format[String] = identity
}

trait StringFormat1 {
  def apply[A](value: A)(implicit format: Format[A]): String

  override def toString: String = apply[String]("$1")
}

object StringFormat1 {
  val Marker = "$1"

  def apply(s1: String, s2: String): StringFormat1 = new StringFormat1 {
    override def apply[A](value: A)(implicit f1: Format[A]): String =
      new StringBuilder().append(s1).append(f1.format(value)).append(s2).result()
  }

  implicit val decoder: Decoder[StringFormat1] = Decoder[String].emap { (value, _) =>
    value.indexOf(Marker) match {
      case -1 => Right(StringFormat0(value))
      case index =>
        val left = value.substring(0, index)
        val right = value.substring(index + Marker.length, value.length)
        Right(StringFormat1(left, right))
    }
  }
}

trait StringFormat0 extends StringFormat1 {
  def apply(): String
}

object StringFormat0 {
  def apply(s0: String): StringFormat0 = new StringFormat0 {
    override def apply(): String = s0

    override def apply[A](value: A)(implicit format: Format[A]): String = s0
  }
}