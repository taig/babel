package io.taig.lokal

import cats.implicits._
import cats.data.NonEmptyList
import io.taig.lokal.imports._

class TranslationTest extends Suite {
  it should "have a String representation" in {
    (de_DE"Hallo" & en"Hello").toString shouldBe
      """de-DE"Hallo" & en"Hello""""
  }

  it should "have a Show instance" in {
    (de_DE"Hallo" & en"Hello").show shouldBe
      (de_DE"Hallo" & en"Hello").toString
  }

  it should "have an Eq instance" in {
    def convertToEqualizer = ???

    val reference = (de_DE"Hallo" & en"Hello")

    reference === reference shouldBe true
    reference === Translation(reference.values.reverse) shouldBe false
    reference === (reference & de_AT"Serwus") shouldBe false
  }

  "&" should "allow to append a Localization" in {
    val de = Localization(LocalizationIdentifier.de, "Hallo")
    val de2 = Localization(LocalizationIdentifier.de_DE, "Hallo")
    (Translation(NonEmptyList.of(de)) & de2).values shouldBe
      NonEmptyList.of(de, de2)
  }

  "translate" should "always prefer an exact identifier match" in {
    val translation = Localization(LocalizationIdentifier.de, "Hallo") &
      Localization(LocalizationIdentifier.de_AT, "Grüß Gott") &
      Localization(LocalizationIdentifier.de_CH, "Hoi")

    translation.translate(LocalizationIdentifier.de) shouldBe "Hallo"
    translation.translate(LocalizationIdentifier.de_AT) shouldBe "Grüß Gott"
    translation.translate(LocalizationIdentifier.de_CH) shouldBe "Hoi"
  }

  it should "otherwise fall back to the unspecified country Localization" in {
    val translation = Localization(LocalizationIdentifier.de, "Hallo") &
      Localization(LocalizationIdentifier.de_AT, "Grüß Gott") &
      Localization(LocalizationIdentifier.de_CH, "Hoi")

    translation.translate(LocalizationIdentifier.de_LU) shouldBe "Hallo"
  }

  it should "otherwise, if not available, fall back to the first Localization of the country" in {
    val translation = Localization(LocalizationIdentifier.de_AT, "Grüß Gott") &
      Localization(LocalizationIdentifier.de_CH, "Hoi")

    translation.translate(LocalizationIdentifier.de_LU) shouldBe "Grüß Gott"
  }

  it should "otherwise, if not available, fall back to the first Localization" in {
    val translation = Localization(LocalizationIdentifier.de, "Hallo") &
      Localization(LocalizationIdentifier.de_AT, "Grüß Gott") &
      Localization(LocalizationIdentifier.de_CH, "Hoi")

    translation.translate(LocalizationIdentifier.en) shouldBe "Hallo"
  }
}
