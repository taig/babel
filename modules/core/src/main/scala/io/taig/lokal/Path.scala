package io.taig.lokal

final case class Path(head: String, tail: List[String]) {
  def toList: List[String] = head +: tail

  def /(segment: String): Path = copy(tail = tail :+ segment)

  def print: String = toList.mkString("/")
}

object Path {
  def one(head: String): Path = Path(head, List.empty)

  def from(value: String): Path = {
    val segments = value.split('/')
    Path(segments.head, segments.tail.toList)
  }
}
