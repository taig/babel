package net.slozzer.babel

import org.scalacheck.Gen

object Generators {
  val language: Gen[Language] = Gen.oneOf(Languages.All)

  val country: Gen[Country] = Gen.oneOf(Countries.All)

  val locale: Gen[Locale] = Gen.oneOf(Locales.All)

  val quantity: Gen[Quantity] = {
    val exact = Gen.chooseNum(-100, 1000).map(Quantity.exact)
    val range = Gen.chooseNum(-100, 1000).flatMap { start =>
      Gen.chooseNum(start, start + 1000).map(Quantity.unsafeRange(start, _))
    }
    Gen.oneOf(exact, range)
  }

  def quantitiesElement[A](value: Gen[A]): Gen[Quantities.Element[A]] =
    for {
      quantity <- quantity
      value <- value
    } yield Quantities.Element(quantity, value)

  def quantities[A](value: Gen[A]): Gen[Quantities[A]] =
    for {
      default <- value
      quantities <- Gen.listOf(quantitiesElement(value))
    } yield Quantities(default, quantities)

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
