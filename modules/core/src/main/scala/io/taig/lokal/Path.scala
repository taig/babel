package io.taig.lokal

final case class Path(head: String, tail: List[String]) {
  def toList: List[String] = head +: tail

  def /(segment: String): Path = copy(tail = tail :+ segment)

  def print: String = toList.mkString("/")

  def printPretty: String = toList.mkString("[", " / ", "]")
}

object Path {
  def one(head: String): Path = Path(head, List.empty)

  def parse(value: String): Option[Path] = value.split('/') match {
    case Array("")   => None
    case Array(head) => Some(Path(head, Nil))
    case segments    => Some(Path(segments.head, segments.tail.toList))
  }

  implicit val printer: Printer[Path] = Printer[String].contramap(_.print)

  implicit val parser: Parser[Path] = Parser[String].emap(parse(_).toRight("Path"))
}
