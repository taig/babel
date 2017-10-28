package io.taig.lokal

import io.taig.lokal.imports._
import org.scalatest.{ FlatSpec, Matchers }

class StringOperationTest extends Suite {
    it should "support interpolation with arbitrary types" in {
        de_DE"Hallo ${42}".value shouldBe "Hallo 42"
        de_DE"Hallo ${"Welt"}".value shouldBe "Hallo Welt"
    }

    it should "support interpolation with Localizations" in {
        val world = de"Welt"
        val greeting = en"Hello $world"
        greeting.value shouldBe "Hello Welt"
    }

    it should "support interpolation with Translations" in {
        val world = en"World" & de"Welt"
        val greeting = en"Hello $world" & de"Hallo $world"
        greeting.translate( LocalizationIdentifier.en ) shouldBe "Hello World"
        greeting.translate( LocalizationIdentifier.de ) shouldBe "Hallo Welt"
    }
}