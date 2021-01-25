package net.slozzer.babel

final case class Quantities(default: String, quantities: Map[Quantity, String]) {
  def apply(quantity: Int): String = quantities.getOrElse(Quantity(quantity), default)

  override def toString: String = if (quantities.isEmpty) s""""$default""""
  else {
    val details = quantities.map { case (quantity, value) => s"""${quantity.value}: "$value"""" }.mkString(", ")
    s"""{$details, *: "$default"}"""
  }
}

object Quantities {
  def one(value: String): Quantities = Quantities(value, Map.empty)
}
