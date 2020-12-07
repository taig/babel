package io.taig.lokal.util

import cats.Invariant
import io.taig.lokal.Quantity
import simulacrum.typeclass
import cats.syntax.all._

@typeclass
trait QuantityEncoder[A] { self =>
  def apply(quantity: Quantity, identifier: String, value: A): A

  final def imap[B](f: A => B)(g: B => A): QuantityEncoder[B] = new QuantityEncoder[B] {
    override def apply(quantity: Quantity, identifier: String, value: B): B =
      f(self.apply(quantity, identifier, g(value)))
  }
}

object QuantityEncoder {
  implicit val string: QuantityEncoder[String] = new QuantityEncoder[String] {
    override def apply(quantity: Quantity, identifier: String, value: String): String =
      value.replace(s"%$identifier", quantity.value.show)
  }

  implicit val invariant: Invariant[QuantityEncoder] = new Invariant[QuantityEncoder] {
    override def imap[A, B](fa: QuantityEncoder[A])(f: A => B)(g: B => A): QuantityEncoder[B] = fa.imap(f)(g)
  }
}
