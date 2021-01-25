package net.slozzer.babel

import java.util.{Locale => JLocale}

final case class Locale(language: Language, country: Option[Country]) {
  def hasCountry: Boolean = country.isDefined

  def withoutCountry: Locale = Locale(language)

  def print(separator: Char): String = country.fold(language.value) { country =>
    language.value + separator + country.value
  }

  def printLanguageTag: String = print('-')

  def printJavaLocaleFormat: String = print('_')

  def toJavaLocale: JLocale = new JLocale(language.value, country.map(_.value).getOrElse(""))

  override def toString: String = printLanguageTag
}

object Locale {
  def apply(language: Language): Locale = Locale(language, country = None)

  def apply(language: Language, country: Country): Locale = Locale(language, Some(country))

  def parse(value: String, separator: Char): Option[Locale] = value.split(separator) match {
    case Array(language) => Language.parse(language).map(Locale(_))
    case Array(language, country) =>
      for {
        language <- Language.parse(language)
        country <- Country.parse(country)
      } yield Locale(language, country)
    case _ => None
  }

  def parseLanguageTag(value: String): Option[Locale] = parse(value, '-')

  def parseJavaLocaleFormat(value: String): Option[Locale] = parse(value, '_')

  def fromJavaLocale(locale: JLocale): Option[Locale] =
    Option(locale.getLanguage)
      .filter(_.nonEmpty)
      .map(Language.apply)
      .map { language =>
        val country = Option(locale.getCountry).filter(_.nonEmpty).map(Country.apply)
        Locale(language, country)
      }

//  implicit val parser: Parser[Locale] =
//    Parser[String].emap(Locale.parseJavaLocaleFormat(_).toRight(Parser.Error("Locale", cause = None)))

  implicit val printer: Printer[Locale] = Printer[String].contramap(_.printLanguageTag)
}
