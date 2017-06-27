package io.taig.lokal

import cats.implicits._
import org.scalatest.{ FlatSpec, Matchers }

class LocalizationTest extends FlatSpec with Matchers {
    it should "have a String representation" in {
        Localization( Identifier.de, "Hallo" ).toString shouldBe
            """de"Hallo""""
        Localization( Identifier.de_DE, "Hallo" ).toString shouldBe
            """de-DE"Hallo""""
    }

    it should "have a Show instance" in {
        Localization( Identifier.de_DE, "Hallo" ).show shouldBe
            Localization( Identifier.de_DE, "Hallo" ).toString
    }

    it should "have an Eq instance" in {
        def convertToEqualizer = ???

        val reference = Localization( Identifier.de_DE, "Hallo" )

        reference === reference shouldBe true
        reference === reference.copy( value = "Hello" ) shouldBe false
        reference === reference.copy( identifier = Identifier.de ) shouldBe false
    }
}