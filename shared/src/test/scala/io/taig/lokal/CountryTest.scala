package io.taig.lokal

import cats.implicits._

class CountryTest extends Suite {
  it should "have an Eq instance" in {
    def convertToEqualizer = ???
    Country.DE === Country.DE shouldBe true
    Country.DE === Country.GB shouldBe false
  }

  it should "have a Show instance" in {
    Country.DE.show shouldBe Country.DE.value
    Country.GB.show shouldBe Country.GB.value
  }
}
