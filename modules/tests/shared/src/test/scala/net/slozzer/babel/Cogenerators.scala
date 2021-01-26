package net.slozzer.babel

import _root_.cats.implicits._
import net.slozzer.babel.cats._
import org.scalacheck.Cogen

object Cogenerators {
  implicit val language: Cogen[Language] = Cogen[String].contramap(_.value)

  implicit val country: Cogen[Country] = Cogen[String].contramap(_.value)

  implicit val locale: Cogen[Locale] = Cogen[(Language, Option[Country])].contramap { locale =>
    (locale.language, locale.country)
  }

  implicit def translations[A: Cogen]: Cogen[Translations[A]] =
    Cogen.cogenMap[Locale, A].contramap(_.values)

  implicit def dictionary[A: Cogen]: Cogen[Dictionary[A]] =
    Cogen[(Translations[A], (Locale, A))].contramap(dictionary => (dictionary.translations, dictionary.fallback))
}
