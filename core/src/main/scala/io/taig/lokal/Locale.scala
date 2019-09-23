package io.taig.lokal

import cats._
import cats.implicits._

final case class Locale(language: Language, country: Option[Country]) {
  def hasCountry: Boolean = country.isDefined

  def withoutCountry: Locale = Locale(language)
}

object Locale extends Locales {
  def apply(language: Language): Locale = Locale(language, country = None)

  def apply(language: Language, country: Country): Locale =
    Locale(language, country.some)

  implicit val eq: Eq[Locale] = (x, y) =>
    x.language === y.language && x.country === y.country

  implicit val show: Show[Locale] = {
    case Locale(language, Some(country)) => show"${language}_$country"
    case Locale(language, None)          => language.show
  }
}
