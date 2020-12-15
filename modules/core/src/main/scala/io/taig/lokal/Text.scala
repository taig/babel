package io.taig.lokal

final case class Text(default: String, quantities: Map[Quantity, String]) {
  def apply(quantity: Quantity): String = quantities.getOrElse(quantity, default)
}

object Text {
  def one(value: String): Text = Text(value, Map.empty)
}
