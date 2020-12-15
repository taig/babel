package io.taig.lokal

import cats.Show
import cats.syntax.all._

final case class Quantity(value: Int) extends AnyVal

object Quantity {
  val Zero: Quantity = Quantity(0)

  val One: Quantity = Quantity(1)

  implicit val show: Show[Quantity] = _.value.show
}
