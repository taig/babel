package io.taig.lokal.util

import cats.Invariant
import io.taig.lokal.Locale

trait ArgumentEncoder[A, B] { self =>
  def apply(locale: Locale, input: A, value: B): B

  final def imap[C](f: B => C)(g: C => B): ArgumentEncoder[A, C] = new ArgumentEncoder[A, C] {
    override def apply(locale: Locale, input: A, value: C): C = f(self.apply(locale, input, g(value)))
  }

  final def contramap[A1](f: A1 => A): ArgumentEncoder[A1, B] = new ArgumentEncoder[A1, B] {
    override def apply(locale: Locale, input: A1, value: B): B = self.apply(locale, f(input), value)
  }
}

object ArgumentEncoder {
  def apply[A, B](implicit encoder: ArgumentEncoder[A, B]): ArgumentEncoder[A, B] = encoder

  implicit def invariant[A]: Invariant[ArgumentEncoder[A, *]] = new Invariant[ArgumentEncoder[A, *]] {
    override def imap[B, C](fa: ArgumentEncoder[A, B])(f: B => C)(g: C => B): ArgumentEncoder[A, C] = fa.imap(f)(g)
  }

  implicit def string1[A: StringFormatter]: ArgumentEncoder[A, String] = new ArgumentEncoder[A, String] {
    override def apply(locale: Locale, input: A, value: String): String = value.format(StringFormatter[A].format(input))
  }

  implicit def string2[A: StringFormatter, B: StringFormatter]: ArgumentEncoder[(A, B), String] =
    new ArgumentEncoder[(A, B), String] {
      override def apply(locale: Locale, input: (A, B), value: String): String = input match {
        case (a, b) => value.format(StringFormatter[A].format(a), StringFormatter[B].format(b))
      }
    }
}
