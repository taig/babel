package io.taig.lokal

import cats.implicits._
import org.scalacheck.Gen

object Generators {
  val locale: Gen[Locale] = Gen.oneOf(Locale.All)

  val rank: Gen[Rank] =
    Gen.oneOf(Rank.Exact, Rank.Country, Rank.Language, Rank.Universal)

  def translationEmpty[A]: Gen[Translation[A]] = Gen.const(Translation.empty)

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
    Gen.listOf(translation(value)).map { translations =>
      translations.combineAll(Translation.monoidK.algebra)
    }
}
