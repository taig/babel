package io.taig.lokal

trait Formatter {
  def format(raw: String, arguments: Seq[Any]): String
}
