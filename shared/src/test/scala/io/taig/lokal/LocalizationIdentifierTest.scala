package io.taig.lokal

import cats.implicits._

class LocalizationIdentifierTest extends Suite {
  it should "allow to compare two Identifiers" in {
    LocalizationIdentifier.de compare LocalizationIdentifier.de shouldBe LocalizationIdentifier.Comparison.Exact
    LocalizationIdentifier.de_DE compare LocalizationIdentifier.de_DE shouldBe LocalizationIdentifier.Comparison.Exact
    LocalizationIdentifier.de compare LocalizationIdentifier.de_DE shouldBe LocalizationIdentifier.Comparison.Weak
    LocalizationIdentifier.de_DE compare LocalizationIdentifier.de shouldBe LocalizationIdentifier.Comparison.Almost
    LocalizationIdentifier.de compare LocalizationIdentifier.en shouldBe LocalizationIdentifier.Comparison.None
    LocalizationIdentifier.de_DE compare LocalizationIdentifier.en shouldBe LocalizationIdentifier.Comparison.None
    LocalizationIdentifier.de compare LocalizationIdentifier.en_US shouldBe LocalizationIdentifier.Comparison.None
    LocalizationIdentifier.de_DE compare LocalizationIdentifier.en_US shouldBe LocalizationIdentifier.Comparison.None
  }

  it should "be able to parse language Identifiers" in {
    LocalizationIdentifier.parse("de") shouldBe Some(LocalizationIdentifier.de)
    LocalizationIdentifier.parse("de-DE") shouldBe Some(
      LocalizationIdentifier.de_DE)
    LocalizationIdentifier.parse("de-DE-AT") shouldBe None
    LocalizationIdentifier.parse("foobar") shouldBe None
  }

  it should "have a String representation" in {
    LocalizationIdentifier.de.toString shouldBe "de"
    LocalizationIdentifier.de_DE.toString shouldBe "de-DE"
  }

  it should "have a Show instance" in {
    LocalizationIdentifier.de_DE.show shouldBe LocalizationIdentifier.de_DE.toString
  }

  it should "have an Eq instance" in {
    def convertToEqualizer = ???
    LocalizationIdentifier.de_DE === LocalizationIdentifier.de_DE shouldBe true
    LocalizationIdentifier.de === LocalizationIdentifier.de_DE shouldBe false
    LocalizationIdentifier.de_DE === LocalizationIdentifier.de shouldBe false
  }
}
