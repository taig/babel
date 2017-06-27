package io.taig.lokal

import cats.implicits._
import org.scalatest.{ FlatSpec, Matchers }

class LanguageTest extends FlatSpec with Matchers {
    it should "have an Eq instance" in {
        def convertToEqualizer = ???
        Language.de === Language.de shouldBe true
        Language.de === Language.en shouldBe false
    }

    it should "have a Show instance" in {
        Language.de.show shouldBe Language.de.value
        Language.en.show shouldBe Language.en.value
    }
}