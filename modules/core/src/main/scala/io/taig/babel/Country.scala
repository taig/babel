package io.taig.babel

final case class Country(value: String) extends AnyVal

object Country {
  def parse(value: String): Option[Country] = if (value.matches("[A-Z]{2}")) Some(Country(value)) else None
}
