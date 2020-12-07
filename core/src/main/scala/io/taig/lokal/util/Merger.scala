package io.taig.lokal.util

import simulacrum.typeclass

@typeclass
trait Merger[A] { self =>
  type Out

  def to(value: A): Out

  def from(value: Out): A

  final def imap[X](f: Out => X)(g: X => Out): Merger.Aux[A, X] = new Merger[A] {
    override type Out = X

    override def to(value: A): X = f(self.to(value))

    override def from(value: X): A = self.from(g(value))
  }
}

object Merger {
  type Aux[A, B] = Merger[A] { type Out = B }
}
