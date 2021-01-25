package net.slozzer.babel

final case class Language(value: String) extends AnyVal

object Language {
  def parse(value: String): Option[Language] = if (value.matches("[a-z]{2}")) Some(Language(value)) else None
}
