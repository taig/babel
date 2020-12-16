package io.taig.babel

final case class Quantity(value: Int) extends AnyVal

object Quantity {
  val Zero: Quantity = Quantity(0)

  val One: Quantity = Quantity(1)

  implicit val parser: Parser[Quantity] = Parser[Int].map(apply)

  implicit val printer: Printer[Quantity] = Printer[Int].contramap(_.value)
}
