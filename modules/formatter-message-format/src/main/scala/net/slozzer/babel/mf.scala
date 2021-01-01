package net.slozzer.babel

import java.text.MessageFormat

object mf {
  implicit val formatterMessageFormat: Formatter = new Formatter {
    override def format(raw: String, arguments: Seq[Any]): String =
      MessageFormat.format(raw, arguments: _*)
  }
}
