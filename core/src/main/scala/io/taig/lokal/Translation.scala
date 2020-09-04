package io.taig.lokal

import cats._
import cats.syntax.all._

import scala.annotation.tailrec

abstract class Translation[+A] {
  def apply(locale: Locale): Option[Result[A]]

  final def translate(locale: Locale): Option[A] = apply(locale).map(_.value)

  final def &[B >: A](translation: Translation[B]): Translation[B] = Translation { locale =>
    val left = apply(locale)
    val right = translation.apply(locale)

    (left, right) match {
      case (Some(left), Some(right)) => (if (left.rank >= right.rank) left else right).some
      case (Some(left), None)        => left.some
      case (None, Some(right))       => right.some
      case (None, None)              => none
    }
  }

  final def map[B](f: A => B): Translation[B] = Translation(locale => apply(locale).map(_.map(f)))

  final def flatMap[B](f: A => Translation[B]): Translation[B] =
    Translation(locale => apply(locale).flatMap(result => f(result.value).apply(locale)))
}

object Translation {
  val Empty: Translation[Nothing] = _ => None

  def apply[A](f: Locale => Option[Result[A]]): Translation[A] = new Translation[A] {
    override def apply(locale: Locale): Option[Result[A]] = f(locale)
  }

  def exact[A](f: Locale => A): Translation[A] = Translation(locale => Result(Rank.Exact, f(locale)).some)

  def one[A](locale: Locale, value: A): Translation[A] = Translation { current =>
    if (locale === current) Result(Rank.Exact, value).some
    else if (locale === current.withoutCountry) Result(Rank.Language, value).some
    else none
  }

  def of[A](translations: List[(Locale, A)]): Translation[A] = Translation { locale =>
    translations
      .find(_._1 === locale)
      .map { case (_, value) => Result(Rank.Exact, value) }
      .orElse {
        translations
          .find(_._1 === locale.withoutCountry)
          .map { case (_, value) => Result(Rank.Language, value) }
      }
  }

  def forAll[A](locales: Locale*)(value: A): Translation[A] = Translation { locale =>
    if (locales.contains(locale)) Result(Rank.Exact, value).some
    else if (locales.contains(locale.withoutCountry)) Result(Rank.Language, value).some
    else none
  }

  def universal[A](value: A): Translation[A] = Translation(_ => Result(Rank.Fallback, value).some)

  implicit val monad: Monad[Translation] = new Monad[Translation] {
    override def pure[A](x: A): Translation[A] = universal(x)

    override def map[A, B](fa: Translation[A])(f: A => B): Translation[B] = fa.map(f)

    override def flatMap[A, B](fa: Translation[A])(f: A => Translation[B]): Translation[B] = fa.flatMap(f)

    override def tailRecM[A, B](a: A)(f: A => Translation[Either[A, B]]): Translation[B] = new Translation[B] {
      @tailrec
      def go(value: A, locale: Locale): Option[Result[B]] =
        f(value)(locale) match {
          case Some(Result(_, Left(a)))     => go(a, locale)
          case Some(Result(rank, Right(b))) => Result(rank, b).some
          case None                         => None
        }

      override def apply(locale: Locale): Option[Result[B]] = go(a, locale)
    }
  }

  implicit val monoidK: MonoidK[Translation] =
    new MonoidK[Translation] {
      override def empty[A]: Translation[A] = Empty

      override def combineK[A](x: Translation[A], y: Translation[A]): Translation[A] = x & y
    }
}
