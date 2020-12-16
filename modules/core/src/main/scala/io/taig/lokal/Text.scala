package io.taig.lokal

final case class Text(default: String, quantities: Map[Quantity, String]) {
  def raw(quantity: Quantity): String = quantities.getOrElse(quantity, default)

  def apply(quantity: Quantity, arguments: Seq[Any])(implicit formatter: Formatter): String = {
    if (arguments.isEmpty) raw(quantity) else formatter.format(raw(quantity), arguments)
  }

  def apply(arguments: Seq[Any])(implicit formatter: Formatter): String = apply(Quantity.One, arguments)

  def apply(quantity: Quantity): String = raw(quantity)

  def apply(): String = default
}

object Text {
  def one(value: String): Text = Text(value, Map.empty)
}
