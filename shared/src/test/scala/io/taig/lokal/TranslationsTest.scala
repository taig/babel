package io.taig.lokal

import cats.data.NonEmptyList
import io.taig.lokal.implicits._
import org.scalatest.{Matchers, WordSpec}

class TranslationsTest extends WordSpec with Matchers {
  val english: Translations[String] = en"Hello"
  val germany: Translations[String] = de_DE"Hallo"
  val austria: Translations[String] = de_AT"Grüß Gott"
  val translations: Translations[String] = english & germany & austria

  "&" should {
    "combine Translations" in {
      germany & austria shouldBe Translations(
        NonEmptyList.of(Translation(Locales.de_DE, "Hallo"),
                        Translation(Locales.de_AT, "Grüß Gott")))
    }
  }

  "resolve" should {
    "prefer the most specific match" in {
      translations.resolve(Locales.de_DE) shouldBe Translation(Locales.de_DE,
                                                               "Hallo")
      translations.resolve(Locales.de_AT) shouldBe Translation(Locales.de_AT,
                                                               "Grüß Gott")
    }

    "fall back to the first matching language" in {
      translations.resolve(Locales.de_CH) shouldBe Translation(Locales.de_DE,
                                                               "Hallo")
    }

    "fall back to a wildcard Translation" in {
      (translations & all"hi")
        .resolve(Locales.es) shouldBe Translation(WildcardLocale, "hi")
    }

    "fall back to the first available Translation" in {
      translations.resolve(Locales.es) shouldBe Translation(Locales.en, "Hello")
    }
  }

  "translate" should {
    "yield the translation value" in {
      translations.translate(Locales.de_DE) shouldBe "Hallo"
      translations.translate(Locales.de_AT) shouldBe "Grüß Gott"
    }
  }
}
