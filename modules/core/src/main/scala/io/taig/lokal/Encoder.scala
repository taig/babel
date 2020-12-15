package io.taig.lokal

import simulacrum.typeclass

@typeclass
trait Encoder[A] {
  def encode(value: A): String
}

object Encoder {
  implicit def fromPrinter[A: Printer]: Encoder[A] = Printer[A].print(_)
}
