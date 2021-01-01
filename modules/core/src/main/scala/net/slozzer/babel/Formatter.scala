package net.slozzer.babel

trait Formatter {
  def format(raw: String, arguments: Seq[Any]): String
}
