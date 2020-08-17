package io.taig.lokal

import cats._
import cats.implicits._

final case class Translation[+A](translations: Map[Locale, A], fallback: Option[A]) {
  def apply(locale: Locale): Option[(Option[Locale], A)] =
    translations.get(locale).tupleLeft(locale.some) orElse
      translations.get(locale.withoutCountry).tupleLeft(locale.withoutCountry.some) orElse
      fallback.tupleLeft(none)

  def translate(locale: Locale): Option[A] = apply(locale).map(_._2)

  def translateOrElse[B >: A](locale: Locale, fallback: => B): B = translate(locale).getOrElse(fallback)

  def translateOrEmpty[B >: A: Monoid](locale: Locale): B = translate(locale).getOrElse(Monoid[B].empty)

  def &[B >: A](translation: Translation[B]): Translation[B] =
    Translation(translations ++ translation.translations, translation.fallback orElse fallback)

  def map[B](f: A => B): Translation[B] = Translation(translations.fmap(f), fallback.map(f))

  def andThen[B](f: A => Translation[B]): Translation[B] = {
    val result = translations.flatMap {
      case (locale, value) => f(value).translate(locale).map(value => Map(locale -> value)).getOrElse(Map.empty)
    }

    Translation(result, fallback.flatMap(f(_).fallback))
  }
}

object Translation {
  val Empty: Translation[Nothing] = Translation(Map.empty, None)

  def one[A](locale: Locale, value: A): Translation[A] =
    Translation(Map(locale -> value), None)

  def of[A](locales: Locale*)(value: A): Translation[A] =
    Translation(locales.toList.tupleRight(value).toMap, None)

  def universal[A](value: A): Translation[A] = Translation(Map.empty, value.some)

  implicit val applicative: Applicative[Translation] = new Applicative[Translation] {
    override def pure[A](x: A): Translation[A] = universal(x)

    override def map[A, B](fa: Translation[A])(f: A => B): Translation[B] = fa.map(f)

    override def product[A, B](fa: Translation[A], fb: Translation[B]): Translation[(A, B)] = {
      val keys = fa.translations.keys ++ fb.translations.keys
      val translations = keys.toList.mapFilter { locale =>
        (fa.translate(locale), fb.translate(locale)).tupled.tupleLeft(locale)
      }.toMap
      Translation(translations, (fa.fallback, fb.fallback).tupled)
    }

    override def ap[A, B](ff: Translation[A => B])(fa: Translation[A]): Translation[B] = {
      val keys = ff.translations.keys ++ fa.translations.keys
      val translations = keys.toList.mapFilter { locale =>
        fa.translate(locale).flatMap(a => ff.translate(locale).map(_.apply(a))).tupleLeft(locale)
      }.toMap

      Translation(translations, fa.fallback.flatMap(a => ff.fallback.map(_.apply(a))))
    }
  }

  implicit val monoidK: MonoidK[Translation] =
    new MonoidK[Translation] {
      override def empty[A]: Translation[A] = Empty

      override def combineK[A](x: Translation[A], y: Translation[A]): Translation[A] = x & y
    }

  implicit def eq[A: Eq]: Eq[Translation[A]] =
    Eq.instance { (x, y) => x.translations === y.translations && x.fallback === y.fallback }

  implicit def show[A: Show]: Show[Translation[A]] = { translation =>
    (translation.translations.toList.map { case (locale, value) => show"""$locale"$value"""" } ++ translation.fallback
      .map(value => show"""*"$value"""")).mkString(" & ")
  }
}
