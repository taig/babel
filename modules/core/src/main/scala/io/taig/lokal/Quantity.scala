package io.taig.lokal

final case class Quantity(value: Int) extends AnyVal

object Quantity {
  val Zero: Quantity = Quantity(0)

  val One: Quantity = Quantity(1)
}
