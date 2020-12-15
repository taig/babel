package io.taig.lokal

object printf {
  implicit val formatterPrintf: Formatter = new Formatter {
    override def format(raw: String, arguments: Seq[Any]): String = raw.format(arguments: _*)
  }
}
