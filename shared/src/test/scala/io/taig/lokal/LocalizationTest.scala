package io.taig.lokal

import cats.data.NonEmptyList
import org.scalatest.{ FlatSpec, Matchers }

class LocalizationTest extends FlatSpec with Matchers {
    it should "allow to append a Localization" in {
        val de = Localization( Identifier.de, "Hallo" )
        val de2 = Localization( Identifier.de_DE, "Hallo" )
        ( Translation( NonEmptyList.of( de ) ) & de2 ).values shouldBe
            NonEmptyList.of( de, de2 )
    }

    it should "have a String representation" in {
        Localization( Identifier.de, "Hallo" ).toString shouldBe
            """de"Hallo""""
        Localization( Identifier.de_DE, "Hallo" ).toString shouldBe
            """de-DE"Hallo""""
    }
}