package io.taig.lokal

import cats.implicits._
import org.scalacheck.Gen

object Generators {
  val locales: List[Locale] = List(
    Locale(Language("de")),
    Locale(Language("de"), Country("AT")),
    Locale(Language("de"), Country("CH")),
    Locale(Language("de"), Country("DE")),
    Locale(Language("en")),
    Locale(Language("en"), Country("GB")),
    Locale(Language("en"), Country("US")),
    Locale(Language("es")),
    Locale(Language("fr"))
  )

  val locale: Gen[Locale] = Gen.oneOf(locales)

  val rank: Gen[Rank] =
    Gen.oneOf(Rank.Exact, Rank.Country, Rank.Language, Rank.Universal)

  def translationEmpty[A]: Gen[Translation[A]] = Gen.const(Translation.Empty)

  def translationUniversal[A](value: Gen[A]): Gen[Translation[A]] =
    value.map(Translation.universal)

  def translationLocale[A](value: Gen[A]): Gen[Translation[A]] =
    for {
      locale <- locale
      value <- value
    } yield Translation(locale, value)

  def translation[A](value: Gen[A]): Gen[Translation[A]] =
    Gen.oneOf(
      translationEmpty[A],
      translationUniversal(value),
      translationLocale(value)
    )

  def translations[A](value: Gen[A]): Gen[Translation[A]] =
    Gen.listOf(translation(value)).map { translations => translations.combineAll(Translation.monoidK.algebra) }
}
