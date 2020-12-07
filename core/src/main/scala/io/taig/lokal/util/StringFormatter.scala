package io.taig.lokal.util

import simulacrum.typeclass

import java.math.BigInteger

@typeclass
trait StringFormatter[A] { self =>
  def format(value: A): Any

  final def contramap[B](f: B => A): StringFormatter[B] = new StringFormatter[B] {
    override def format(value: B): Any = self.format(f(value))
  }
}

object StringFormatter {
  def instance[A](f: A => Any): StringFormatter[A] = new StringFormatter[A] {
    override def format(value: A): Any = f(value)
  }

  implicit val string: StringFormatter[String] = instance(identity)

  implicit val byte: StringFormatter[Byte] = instance(identity)

  implicit val short: StringFormatter[Short] = instance(identity)

  implicit val int: StringFormatter[Int] = instance(identity)

  implicit val long: StringFormatter[Long] = instance(identity)

  implicit val float: StringFormatter[Float] = instance(identity)

  implicit val double: StringFormatter[Double] = instance(identity)

  implicit val javaBigInteger: StringFormatter[java.math.BigInteger] = instance(identity)

  implicit val javaBigDecimal: StringFormatter[java.math.BigDecimal] = instance(identity)

  implicit val bigInt: StringFormatter[BigInt] = StringFormatter[java.math.BigInteger].contramap(_.bigInteger)

  implicit val bigDecimal: StringFormatter[BigDecimal] = StringFormatter[java.math.BigDecimal].contramap(_.bigDecimal)
}
