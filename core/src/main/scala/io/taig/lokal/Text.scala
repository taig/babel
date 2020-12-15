package io.taig.lokal

final case class Text(default: String, quantities: Map[Quantity, String]) {
  def apply(quantity: Quantity): String = quantities.getOrElse(quantity, default)
}
