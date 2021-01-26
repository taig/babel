package net.slozzer.babel

import org.scalacheck.Gen

object Generators {
  val language: Gen[Language] = Gen.oneOf(Languages.All)

  val country: Gen[Country] = Gen.oneOf(Countries.All)

  val locale: Gen[Locale] = Gen.oneOf(Locales.All)

  private def localeA[A](value: Gen[A]): Gen[(Locale, A)] =
    for {
      locale <- locale
      value <- value
    } yield (locale, value)

  def translations[A](value: Gen[A]): Gen[Translations[A]] = Gen.mapOf(localeA(value)).map(Translations.apply)

  def dictionary[A](value: Gen[A]): Gen[Dictionary[A]] =
    for {
      translations <- translations(value)
      fallback <- localeA(value)
    } yield Dictionary(translations, fallback)
}
