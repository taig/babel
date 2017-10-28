package io.taig.lokal

import cats.implicits._
import org.scalatest.{ FlatSpec, Matchers }

class LocalizationTest extends Suite {
    it should "have a String representation" in {
        Localization( LocalizationIdentifier.de, "Hallo" ).toString shouldBe
        """de"Hallo""""
        Localization( LocalizationIdentifier.de_DE, "Hallo" ).toString shouldBe
        """de-DE"Hallo""""
    }

    it should "have a Show instance" in {
        Localization( LocalizationIdentifier.de_DE, "Hallo" ).show shouldBe
        Localization( LocalizationIdentifier.de_DE, "Hallo" ).toString
    }

    it should "have an Eq instance" in {
        def convertToEqualizer = ???

        val reference = Localization( LocalizationIdentifier.de_DE, "Hallo" )

        reference === reference shouldBe true
        reference === reference.copy( value = "Hello" ) shouldBe false
        reference === reference.copy( identifier = LocalizationIdentifier.de ) shouldBe false
    }
}