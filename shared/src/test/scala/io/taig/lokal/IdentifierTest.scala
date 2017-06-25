package io.taig.lokal

import org.scalatest.{ FlatSpec, Matchers }

class IdentifierTest extends FlatSpec with Matchers {
    it should "allow to compare two Identifiers" in {
        Identifier.de compare Identifier.de shouldBe Identifier.Comparison.Exact
        Identifier.de_DE compare Identifier.de_DE shouldBe Identifier.Comparison.Exact
        Identifier.de compare Identifier.de_DE shouldBe Identifier.Comparison.Weak
        Identifier.de_DE compare Identifier.de shouldBe Identifier.Comparison.Almost
        Identifier.de compare Identifier.en shouldBe Identifier.Comparison.None
        Identifier.de_DE compare Identifier.en shouldBe Identifier.Comparison.None
        Identifier.de compare Identifier.en_US shouldBe Identifier.Comparison.None
        Identifier.de_DE compare Identifier.en_US shouldBe Identifier.Comparison.None
    }

    it should "be able to parse language Identifiers" in {
        Identifier.parse( "de" ) shouldBe Some( Identifier.de )
        Identifier.parse( "de-DE" ) shouldBe Some( Identifier.de_DE )
        Identifier.parse( "de-DE-AT" ) shouldBe None
        Identifier.parse( "foobar" ) shouldBe None
    }

    it should "have a String representation" in {
        Identifier.de.toString shouldBe "de"
        Identifier.de_DE.toString shouldBe "de-DE"
    }
}