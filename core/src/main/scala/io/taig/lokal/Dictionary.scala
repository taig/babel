package io.taig.lokal

import cats.implicits._
import cats.{Monad, SemigroupK}

import scala.annotation.tailrec

/** A non-empty set of translations that will always yield a (fallback) translation for any given locale */
abstract class Dictionary[+A] {
  def apply(locale: Locale): Result[A]

  final def translate(locale: Locale): A = apply(locale).value

  final def ++[B >: A](dictionary: Dictionary[B]): Dictionary[B] = Dictionary { locale =>
    val left = apply(locale)
    val right = dictionary.apply(locale)
    if (left.rank >= right.rank) left else if (right.rank > Rank.Fallback) right else left
  }

  final def append[B >: A](translation: Translation[B]): Dictionary[B] = Dictionary { locale =>
    val left = apply(locale)
    val right = translation.apply(locale)
    right.fold[Result[B]](left)(right => if (left.rank >= right.rank) left else right)
  }

  final def &[B >: A](translation: Translation[B]): Dictionary[B] = append(translation)

  final def prepend[B >: A](translation: Translation[B]): Dictionary[B] = Dictionary { locale =>
    val left = translation.apply(locale)
    val right = apply(locale)
    left.fold[Result[B]](right)(left => if (left.rank >= right.rank) left else right)
  }

  final def map[B](f: A => B): Dictionary[B] = Dictionary(locale => apply(locale).map(f))

  final def flatMap[B](f: A => Dictionary[B]): Dictionary[B] =
    Dictionary(locale => f(apply(locale).value).apply(locale))
}

object Dictionary {
  def apply[A](f: Locale => Result[A]): Dictionary[A] = new Dictionary[A] {
    override def apply(locale: Locale): Result[A] = f(locale)
  }

  def exact[A](f: Locale => A): Dictionary[A] = Dictionary(locale => Result(Rank.Exact, f(locale)))

  def one[A](locale: Locale, value: A, fallback: => A): Dictionary[A] = Dictionary { current =>
    if (locale === current) Result(Rank.Exact, value)
    else if (locale === current.withoutCountry) Result(Rank.Language, value)
    else Result(Rank.Fallback, fallback)
  }

  def universal[A](value: A): Dictionary[A] = Dictionary(_ => Result(Rank.Fallback, value))

  implicit val monad: Monad[Dictionary] = new Monad[Dictionary] {
    override def pure[A](x: A): Dictionary[A] = universal(x)

    override def map[A, B](fa: Dictionary[A])(f: A => B): Dictionary[B] = fa.map(f)

    override def flatMap[A, B](fa: Dictionary[A])(f: A => Dictionary[B]): Dictionary[B] = fa.flatMap(f)

    override def tailRecM[A, B](a: A)(f: A => Dictionary[Either[A, B]]): Dictionary[B] = new Dictionary[B] {
      @tailrec
      def go(value: A, locale: Locale): Result[B] =
        f(value)(locale) match {
          case Result(_, Left(a))     => go(a, locale)
          case Result(rank, Right(b)) => Result(rank, b)
        }

      override def apply(locale: Locale): Result[B] =
        go(a, locale)
    }
  }

  implicit val semigroupK: SemigroupK[Dictionary] = new SemigroupK[Dictionary] {
    override def combineK[A](x: Dictionary[A], y: Dictionary[A]): Dictionary[A] = x ++ y
  }
}
