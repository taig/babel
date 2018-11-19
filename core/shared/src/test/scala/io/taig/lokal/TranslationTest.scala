package io.taig.lokal

import io.taig.lokal.implicits._
import org.scalatest.{Matchers, WordSpec}

class TranslationTest extends WordSpec with Matchers {
  val fallback: Translation[String] = xx"Hello"
  val germany: Translation[Option[String]] = de"Hallo"
  val austria: Translation[Option[String]] = de_AT"Grüß Gott"
  val translations: Translation[String] = fallback & germany & austria

  "translate" should {
    "pick the matching Locale" in {
      translations(Locales.de_AT) shouldBe "Grüß Gott"
    }

    "fall back to the language if the country is not available" in {
      translations(Locales.de_DE) shouldBe "Hallo"
    }

    "ball back to the wildcard if no matching language is available" in {
      translations(Locales.es) shouldBe "Hello"
    }
  }
}
