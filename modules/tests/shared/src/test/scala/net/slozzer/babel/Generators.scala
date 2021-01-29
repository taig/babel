package net.slozzer.babel

import org.scalacheck.Gen

object Generators {
  val language: Gen[Language] = Gen.oneOf(Languages.All)

  val country: Gen[Country] = Gen.oneOf(Countries.All)

  val locale: Gen[Locale] = Gen.oneOf(Locales.All)

  def translation[A](value: Gen[A]): Gen[Translation[A]] =
    for {
      locale <- locale
      value <- value
    } yield Translation(locale, value)

  def translations[A](value: Gen[A]): Gen[Translations[A]] = Gen.listOf(translation(value)).map(Translations.from)

  def nonEmptyTranslations[A](value: Gen[A]): Gen[NonEmptyTranslations[A]] =
    for {
      default <- translation(value)
      translations <- translations(value)
    } yield NonEmptyTranslations(default, translations)
}
