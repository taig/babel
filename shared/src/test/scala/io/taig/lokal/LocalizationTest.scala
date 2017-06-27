package io.taig.lokal

import org.scalatest.{ FlatSpec, Matchers }

class LocalizationTest extends FlatSpec with Matchers {
    it should "have a String representation" in {
        Localization( Identifier.de, "Hallo" ).toString shouldBe
            """de"Hallo""""
        Localization( Identifier.de_DE, "Hallo" ).toString shouldBe
            """de-DE"Hallo""""
    }
}