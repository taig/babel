package io.taig.lokal

import java.util.Locale

import org.scalacheck.Gen

object Generators {
  val locale: Gen[Locale] = Gen.oneOf(Locales.All)

  def payload[A](value: Gen[A]): Gen[(Locale, A)] =
    for {
      locale <- locale
      value <- value
    } yield (locale, value)

  def translation[A](value: Gen[A]): Gen[Translation[A]] =
    for {
      (locale, value) <- payload(value)
      translations <- Gen.listOf(payload(value)).map(_.toMap)
    } yield Translation(locale, value, translations)
}
