package io.taig.lokal

import io.taig.lokal.implicits._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class TranslationTest extends AnyWordSpec with Matchers {
  val english: Translation[String] = en"Hello"
  val germany: Translation[String] = de"Hallo"
  val austria: Translation[String] = de_AT"Grüß Gott"
  val translations: Translation[String] = english & germany & austria

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
