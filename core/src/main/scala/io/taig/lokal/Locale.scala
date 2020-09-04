package io.taig.lokal

import cats._
import cats.syntax.all._
import java.util.{Locale => JLocale}

final case class Locale(language: Language, country: Option[Country]) {
  def hasCountry: Boolean = country.isDefined

  def withoutCountry: Locale = Locale(language)

  def toJavaLocale: JLocale = new JLocale(language.value, country.map(_.value).getOrElse(""))
}

object Locale {
  def apply(language: Language): Locale = Locale(language, country = None)

  def apply(language: Language, country: Country): Locale =
    Locale(language, country.some)

  def fromJavaLocale(locale: JLocale): Option[Locale] =
    Option(locale.getLanguage)
      .filter(_.nonEmpty)
      .map(Language.apply)
      .map { language =>
        val country = Option(locale.getCountry).filter(_.nonEmpty).map(Country.apply)
        Locale(language, country)
      }

  implicit val eq: Eq[Locale] = (x, y) => x.language === y.language && x.country === y.country

  implicit val show: Show[Locale] = {
    case Locale(language, Some(country)) => show"${language}_$country"
    case Locale(language, None)          => language.show
  }
}
