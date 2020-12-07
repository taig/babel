package io.taig.lokal

import cats.Invariant

trait Encoder[A, B] { self =>
  def apply(locale: Locale, input: A, value: B): B

  final def imap[C](f: B => C)(g: C => B): Encoder[A, C] = new Encoder[A, C] {
    override def apply(locale: Locale, input: A, value: C): C = f(self.apply(locale, input, g(value)))
  }

  final def contramap[A1](f: A1 => A): Encoder[A1, B] = new Encoder[A1, B] {
    override def apply(locale: Locale, input: A1, value: B): B = self.apply(locale, f(input), value)
  }
}

object Encoder extends Encoder1 {
  def apply[A, B](implicit encoder: Encoder[A, B]): Encoder[A, B] = encoder

  implicit def invariant[A]: Invariant[Encoder[A, *]] = new Invariant[Encoder[A, *]] {
    override def imap[B, C](fa: Encoder[A, B])(f: B => C)(g: C => B): Encoder[A, C] = fa.imap(f)(g)
  }

  implicit def translation1[A](implicit encoder: Encoder[A, A]): Encoder[Translation[Any, A], A] =
    new Encoder[Translation[Any, A], A] {
      override def apply(locale: Locale, input: Translation[Any, A], value: A): A =
        encoder(locale, input.translate(locale, ()), value)
    }
}

trait Encoder1 extends Encoder2 {
  implicit def string2[A, B]: Encoder[(A, B), String] = new Encoder[(A, B), String] {
    override def apply(locale: Locale, input: (A, B), value: String): String = input match {
      case (a, b) => value.format(a, b)
    }
  }
}

trait Encoder2 {
  implicit def string1[A]: Encoder[A, String] = new Encoder[A, String] {
    override def apply(locale: Locale, input: A, value: String): String = value.format(input)
  }
}
