package io.taig.lokal.util

import simulacrum.typeclass

@typeclass
trait Encoder[A] {
  def apply(value: A): String
}
