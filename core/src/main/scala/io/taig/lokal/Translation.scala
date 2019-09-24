package io.taig.lokal

import cats._
import cats.implicits._

import scala.annotation.tailrec

abstract class Translation[A] {
  def apply(locale: Locale): Option[(Rank, A)]

  final def translate(locale: Locale): Option[A] = apply(locale).map(_._2)

  final def translateOrElse(locale: Locale, fallback: => A): A =
    translate(locale).getOrElse(fallback)

  final def translateOrEmpty(locale: Locale)(implicit A: Monoid[A]): A =
    translate(locale).orEmpty

  final def &(translation: Translation[A]): Translation[A] = { locale =>
    (apply(locale), translation(locale)) match {
      case (x @ Some((rankX, _)), y @ Some((rankY, _))) =>
        if (rankY >= rankX) y else x
      case (None, y @ Some(_)) => y
      case (x @ Some(_), None) => x
      case (None, None)        => None
    }
  }

  final def map[B](f: A => B): Translation[B] = apply(_).map(_.map(f))

  final def flatMap[B](f: A => Translation[B]): Translation[B] =
    locale =>
      apply(locale).flatMap {
        case (_, value) => f(value)(locale)
      }
}

object Translation {
  def empty[A]: Translation[A] = _ => None

  def apply[A](locale: Locale, value: A): Translation[A] =
    current =>
      if (locale === current) (Rank.Exact, value).some
      else if (locale === current.withoutCountry) (Rank.Language, value).some
      else if (locale.language === current.language) (Rank.Country, value).some
      else None

  def of[A](locales: Locale*)(value: A): Translation[A] =
    locales.map(apply(_, value)).toList.combineAll(monoidK.algebra)

  def universal[A](value: A): Translation[A] = _ => (Rank.Universal, value).some

  implicit val monad: Monad[Translation] = new Monad[Translation] {
    override def pure[A](x: A): Translation[A] = universal(x)

    override def map[A, B](fa: Translation[A])(f: A => B): Translation[B] =
      fa.map(f)

    override def flatMap[A, B](fa: Translation[A])(
        f: A => Translation[B]
    ): Translation[B] = fa.flatMap(f)

    override def tailRecM[A, B](
        a: A
    )(f: A => Translation[Either[A, B]]): Translation[B] = new Translation[B] {
      @tailrec
      def go(value: A, locale: Locale): Option[(Rank, B)] =
        f(value)(locale) match {
          case Some((_, Left(a)))     => go(a, locale)
          case Some((rank, Right(b))) => (rank, b).some
          case None                   => None
        }

      override def apply(locale: Locale): Option[(Rank, B)] =
        go(a, locale)
    }
  }

  implicit val monoidK: MonoidK[Translation] =
    new MonoidK[Translation] {
      override def empty[A]: Translation[A] = Translation.empty

      override def combineK[A](
          x: Translation[A],
          y: Translation[A]
      ): Translation[A] =
        x & y
    }
}
