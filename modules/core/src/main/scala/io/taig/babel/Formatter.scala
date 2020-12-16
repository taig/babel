package io.taig.babel

trait Formatter {
  def format(raw: String, arguments: Seq[Any]): String
}
