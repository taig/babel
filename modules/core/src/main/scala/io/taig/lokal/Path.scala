package io.taig.lokal

final case class Path(values: List[String]) {
  def /(segment: String): Path = Path(values :+ segment)

  def print: String = values.mkString("/")

  def printPretty: String = values.mkString("[", " / ", "]")
}

object Path {
  val Empty: Path = Path(List.empty)

  def one(head: String): Path = Path(List(head))

  def parse(value: String): Path = value.split('/') match {
    case Array("")   => Empty
    case Array(head) => one(head)
    case segments    => apply(segments.toList)
  }

  implicit val printer: Printer[Path] = Printer[String].contramap(_.print)

  implicit val parser: Parser[Path] = Parser[String].map(parse)
}
