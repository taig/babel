package net.slozzer.babel

import simulacrum.typeclass

@typeclass
trait Encoder[A] {
  def encode(value: A): Babel
}
