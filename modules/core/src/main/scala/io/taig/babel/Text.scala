package io.taig.babel

final case class Text(default: String, quantities: Map[Quantity, String]) {
  def raw(quantity: Int): String = quantities.getOrElse(Quantity(quantity), default)

  def apply(quantity: Int, arguments: Seq[Any])(implicit formatter: Formatter): String = {
    if (arguments.isEmpty) raw(quantity) else formatter.format(raw(quantity), arguments)
  }

  def apply(arguments: Seq[Any])(implicit formatter: Formatter): String = apply(quantity = 1, arguments)

  def apply(quantity: Int): String = raw(quantity)

  def apply(): String = default

  override def toString: String = if (quantities.isEmpty) s""""$default""""
  else {
    val details = quantities.map { case (quantity, value) => s"""${quantity.value}: "$value"""" }.mkString(", ")
    s"""{$details, *: "$default"}"""
  }
}

object Text {
  def one(value: String): Text = Text(value, Map.empty)
}
