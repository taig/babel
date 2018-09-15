package io.taig.lokal.instances

import java.util.Locale

import cats.implicits._
import cats.{Eq, Show}

trait LocaleInstances {
  implicit def lokalLocaleEq: Eq[Locale] =
    (x: Locale, y: Locale) =>
      x.getLanguage === y.getLanguage && x.getCountry === y.getCountry

  implicit def lokalLocaleShow: Show[Locale] = { locale =>
    (locale.getLanguage, locale.getCountry) match {
      case (language, "")      => language
      case ("", country)       => country
      case (language, country) => s"${language}_$country"
    }
  }
}
