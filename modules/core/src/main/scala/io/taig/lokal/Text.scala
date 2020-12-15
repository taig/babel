package io.taig.lokal

final case class Text(default: String, quantities: Map[Quantity, String]) {
  def apply(quantity: Quantity, arguments: Seq[Any])(implicit formatter: Formatter): String =
    formatter.format(quantities.getOrElse(quantity, default), arguments)

  def apply(arguments: Seq[Any])(implicit formatter: Formatter): String = apply(Quantity.One, arguments)

  def apply(quantity: Quantity): String = quantities.getOrElse(quantity, default)

  def apply(): String = default
}

object Text {
  def one(value: String): Text = Text(value, Map.empty)
}
