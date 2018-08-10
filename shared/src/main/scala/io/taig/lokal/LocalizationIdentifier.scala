package io.taig.lokal

import cats.implicits._
import cats.{Eq, Show}

case class LocalizationIdentifier(language: Language,
                                  country: Option[Country]) {
  def compare(
      identifier: LocalizationIdentifier): LocalizationIdentifier.Comparison =
    if (this == identifier) LocalizationIdentifier.Comparison.Exact
    else if (this.language == identifier.language)
      if (this.country.isDefined && identifier.country.isEmpty)
        LocalizationIdentifier.Comparison.Almost
      else LocalizationIdentifier.Comparison.Weak
    else LocalizationIdentifier.Comparison.None

  override def toString: String = country.fold(language.value) { country ⇒
    s"${language.value}-${country.value}"
  }
}

object LocalizationIdentifier extends LocalizationIdentifiers {
  sealed trait Comparison extends Product with Serializable

  object Comparison {
    case object Exact extends Comparison
    case object Almost extends Comparison
    case object Weak extends Comparison
    case object None extends Comparison
  }

  implicit val eq: Eq[LocalizationIdentifier] = Eq.instance { (a, b) ⇒
    a.language === b.language && a.country === b.country
  }

  implicit val show: Show[LocalizationIdentifier] = Show.fromToString

  def parse(identifier: String): Option[LocalizationIdentifier] =
    identifier.split("-") match {
      case Array(language) if language.length == 2 ⇒
        Some(LocalizationIdentifier(Language(language), None))
      case Array(language, country)
          if language.length == 2 && country.length == 2 ⇒
        val identifier = LocalizationIdentifier(
          Language(language),
          Some(Country(country))
        )
        Some(identifier)
      case _ ⇒ None
    }
}
