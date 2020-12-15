package io.taig.lokal

import simulacrum.typeclass

@typeclass
trait Decoder[A] {
  def decode(values: Map[Path, A]): Either[String, A]
}

//object Decoder {
//  implicit def fromParser[A: Parser]: Decoder[A] = Parser[A].parse(_)
//}
