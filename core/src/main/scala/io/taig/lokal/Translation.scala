package io.taig.lokal

import java.util.Locale

import cats.data.NonEmptyMap
import cats.implicits._
import cats.{Eq, FlatMap, Semigroup, SemigroupK, Show}
import io.taig.lokal.implicits._

final case class Translation[A](
    locale: Locale,
    value: A,
    translations: Map[Locale, A]
) extends (Locale => A) {
  override def apply(locale: Locale): A =
    resolve(locale, toMap).getOrElse(value)

  private def resolve(locale: Locale, translations: Map[Locale, A]): Option[A] =
    if (locale.getCountry == "") translations.get(locale)
    else
      translations
        .get(locale)
        .orElse(translations.get(new Locale(locale.getLanguage)))

  def locales: Set[Locale] = translations.keys.toSet + locale

  def values: Set[A] = translations.values.toSet + value

  def toMap: Map[Locale, A] = translations + (locale -> value)

  def &(translation: Translation[A]): Translation[A] = {
    val translations = toMap ++ translation.toMap
    Translation(locale, translations(locale), translations.removed(locale))
  }

  override def toString(): String = this.map(_.toString).show
}

object Translation {
  def apply[A](locale: Locale, value: A): Translation[A] =
    Translation(locale, value, Map.empty)

  implicit val flatMap: FlatMap[Translation] = new FlatMap[Translation] {
    override def map[A, B](fa: Translation[A])(f: A => B): Translation[B] =
      Translation(
        fa.locale,
        f(fa.value),
        fa.translations.fmap(f)
      )

    override def flatMap[A, B](
        fa: Translation[A]
    )(f: A => Translation[B]): Translation[B] =
      Translation(fa.locale, f(fa.value)(fa.locale), fa.translations.map {
        case (locale, value) => (locale, f(value)(locale))
      })

    // TODO @tailrec
    override def tailRecM[A, B](
        a: A
    )(f: A => Translation[Either[A, B]]): Translation[B] =
      f(a) match {
        case Translation(locale, Left(a), translations) =>
          Translation(locale, tailRecM(a)(f)(locale), translations.map {
            case (locale, Left(a))  => locale -> tailRecM(a)(f)(locale)
            case (locale, Right(b)) => locale -> b
          })
        case Translation(locale, Right(b), translations) =>
          Translation(locale, b, translations.map {
            case (locale, Left(a))  => locale -> tailRecM(a)(f)(locale)
            case (locale, Right(b)) => locale -> b
          })
      }
  }

  implicit def semigroup[A: Semigroup]: Semigroup[Translation[A]] = { (x, y) =>
    val locales = x.locales ++ y.locales - x.locale

    val translations = locales.map { locale =>
      locale -> (x(locale) |+| y(locale))
    }.toMap

    Translation(x.locale, x.value |+| y(x.locale), translations)
  }

  implicit val semigroupK: SemigroupK[Translation] =
    new SemigroupK[Translation] {
      override def combineK[A](
          x: Translation[A],
          y: Translation[A]
      ): Translation[A] = {
        val translations = (x.toMap ++ y.toMap) - y.locale
        Translation(y.locale, y.value, translations)
      }
    }

  implicit def show[A: Show]: Show[Translation[A]] = Show.show { translation =>
    translation.toMap
      .map { case (locale, value) => show"""$locale"$value"""" }
      .mkString("Translation(", ", ", ")")
  }

  implicit def eq[A: Eq]: Eq[Translation[A]] = _.toMap === _.toMap
}
