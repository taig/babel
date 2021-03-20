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
  final case class Exact(value: Int) extends Quantity

  final case class Range(start: Int, end: Int) extends Quantity

  val Zero: Quantity = exact(0)

  val One: Quantity = exact(1)

  def exact(value: Int): Quantity = Exact(value)

  def unsafeRange(start: Int, end: Int): Quantity = Range(start, end)

  def range(start: Int, end: Int): Option[Quantity] = if (start == end) Some(exact(start))
  else if (end >= start) Some(unsafeRange(start, end))
  else None

  private val regex = "^(-?\\d+)(?:-(-?\\d+))?$".r

  def parse(value: String): Either[String, Quantity] =
    regex.findFirstMatchIn(value).toRight("Invalid quantity").flatMap { matsch =>
      matsch.group(1).toIntOption.toRight("Invalid integer").flatMap { start =>
        Option(matsch.group(2)) match {
          case Some(end) =>
            end.toIntOption.toRight("Invalid integer").flatMap(range(start, _).toRight("Invalid range"))
          case None => Right(exact(start))
        }
      }
    }
}
