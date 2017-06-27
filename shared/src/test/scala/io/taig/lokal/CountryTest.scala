package io.taig.lokal

import cats.implicits._
import org.scalatest.{ FlatSpec, Matchers }

class CountryTest extends FlatSpec with Matchers {
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