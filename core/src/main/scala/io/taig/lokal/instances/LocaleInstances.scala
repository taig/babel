package io.taig.lokal.instances

import java.util.Locale

import cats.implicits._
import cats.{Eq, Show}

trait LocaleInstances {
  implicit val lokalLocaleEq: Eq[Locale] =
    (x, y) => x.getLanguage === y.getLanguage && x.getCountry === y.getCountry

  implicit val lokalLocaleShow: Show[Locale] = { locale =>
    (locale.getLanguage, locale.getCountry) match {
      case (language, "")      => language
      case ("", country)       => country
      case (language, country) => s"${language}_$country"
    }
  }
}
