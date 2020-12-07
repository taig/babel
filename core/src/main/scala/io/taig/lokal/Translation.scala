package io.taig.lokal

import cats.Invariant
import cats.syntax.all._

sealed abstract class Translation[-A, B] extends Product with Serializable {
  final def apply(locale: Locale, arguments: A): Option[Result[B]] = this match {
    case Translation.Specific(actual, translation) =>
      if (locale === actual) translation(locale, arguments).map(_.copy(rank = Rank.Exact))
      else if (locale.withoutCountry === actual) translation(locale, arguments).map(_.copy(rank = Rank.Language))
      else None
    case Translation.Universal(value)          => Some(Result(Rank.Fallback, value))
    case Translation.Arguments(encoder, value) => Some(Result(Rank.Fallback, encoder(arguments, value)))
    case Translation.Or(left, right) =>
      List(left(locale, arguments), right(locale, arguments)).flatten.maximumByOption(_.rank)
  }

  final def ~[A1 <: A](translation: Translation[A1, B]): Translation[A1, B] =
    Translation.Or(this, translation)

  final def imap[C](f: B => C)(g: C => B): Translation[A, C] = this match {
    case Translation.Specific(locale, translation) => Translation.Specific(locale, translation.imap(f)(g))
    case Translation.Universal(value)              => Translation.Universal(f(value))
    case Translation.Arguments(encoder, value)     => Translation.Arguments(encoder.imap(f)(g), f(value))
    case Translation.Or(left, right)               => Translation.Or(left.imap(f)(g), right.imap(f)(g))
  }
}

object Translation {
  final case class Specific[A, B](locale: Locale, translation: Translation[A, B]) extends Translation[A, B]
  final case class Universal[A](value: A) extends Translation[Any, A]
  final case class Arguments[A, B](encoder: Encoder[A, B], value: B) extends Translation[A, B]
  final case class Or[A, B](left: Translation[A, B], right: Translation[A, B]) extends Translation[A, B]

  def apply[A, B](locale: Locale, translation: Translation[A, B]): Specific[A, B] = Specific(locale, translation)

  def universal[A](value: A): Translation[Any, A] = Universal(value)

  def arguments[A, B](value: B)(implicit encoder: Encoder[A, B]): Translation[A, B] = Arguments(encoder, value)

  def of[A](locale: Locale, value: A): Specific[Any, A] = Translation(locale, universal(value))

  def via[A, B](locale: Locale, value: B)(implicit encoder: Encoder[A, B]): Translation[A, B] =
    Translation(locale, Arguments(encoder, value))

  implicit def invariant[A]: Invariant[Translation[A, *]] = new Invariant[Translation[A, *]] {
    override def imap[B, C](fa: Translation[A, B])(f: B => C)(g: C => B): Translation[A, C] = fa.imap(f)(g)
  }
}
