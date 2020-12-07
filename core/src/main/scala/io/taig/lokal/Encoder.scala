package io.taig.lokal

import cats.{Invariant, Show}
import cats.syntax.all._

trait Encoder[A, B] { self =>
  def apply(input: A, value: B): B

  final def imap[C](f: B => C)(g: C => B): Encoder[A, C] = new Encoder[A, C] {
    override def apply(input: A, value: C): C = f(self.apply(input, g(value)))
  }
}

object Encoder {
  def apply[A, B](implicit encoder: Encoder[A, B]): Encoder[A, B] = encoder

  implicit def invariant[A]: Invariant[Encoder[A, *]] = new Invariant[Encoder[A, *]] {
    override def imap[B, C](fa: Encoder[A, B])(f: B => C)(g: C => B): Encoder[A, C] = fa.imap(f)(g)
  }

  implicit def string1[A]: Encoder[A, String] = new Encoder[A, String] {
    override def apply(input: A, value: String): String = value.format(input)
  }

  implicit def string2[A, B]: Encoder[(A, B), String] = new Encoder[(A, B), String] {
    override def apply(input: (A, B), value: String): String = input match {
      case (a, b) => value.format(a, b)
    }
  }
}
