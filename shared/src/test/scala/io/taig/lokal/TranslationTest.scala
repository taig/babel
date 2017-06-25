package io.taig.lokal

import io.taig.lokal.imports._
import org.scalatest.{ FlatSpec, Matchers }

class TranslationTest extends FlatSpec with Matchers {
    it should "have a String representation" in {
        ( de_DE"Hallo" & en"Hello" ).toString shouldBe
            """de-DE"Hallo" & en"Hello""""
    }
}