package io.taig.babel

final case class Path(values: List[String]) {
  def /(segment: String): Path = Path(values :+ segment)

  def print: String = values.mkString("/")

  def printPretty: String = values.mkString(" / ")

  def printPlaceholder: String = values.mkString("[", " / ", "]")
}

object Path {
  val Root: Path = Path(List.empty)

  def one(head: String): Path = Path(List(head))

  def parse(value: String): Path = value.split('/') match {
    case Array("")   => Root
    case Array(head) => one(head)
    case segments    => apply(segments.toList)
  }
}
