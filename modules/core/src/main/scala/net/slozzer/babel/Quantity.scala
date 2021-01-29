package net.slozzer.babel

sealed abstract class Quantity extends Product with Serializable {
  def matches(quantity: Int): Boolean = this match {
    case Quantity.Exact(value)      => value == quantity
    case Quantity.Range(start, end) => quantity >= start && quantity <= end
  }

  final override def toString: String = this match {
    case Quantity.Exact(value)      => s"$value"
    case Quantity.Range(start, end) => s"$start-$end"
  }
}

object Quantity {
  final case class Exact private (value: Int) extends Quantity

  final case class Range private (start: Int, end: Int) extends Quantity

  val Zero: Quantity = exact(0)

  val One: Quantity = exact(1)

  def exact(value: Int): Quantity = Exact(value)

  def unsafeRange(start: Int, end: Int): Quantity = Range(start, end)

  def range(start: Int, end: Int): Option[Quantity] = Option.when(end >= start)(unsafeRange(start, end))

  def parse(value: String): Either[String, Quantity] = value.split("-", 2) match {
    case Array(value) => value.toIntOption.toRight(s"Not a valid integer: $value").map(exact)
    case Array(start, end) =>
      for {
        start <- start.toIntOption.toRight(s"Not a valid integer: $start")
        end <- end.toIntOption.toRight(s"Not a valid integer: $end")
        range <- range(start, end).toRight("Invalid range values")
      } yield range
    case _ => Left("Invalid range")
  }
}
