package io.taig.lokal.dsl

import cats.implicits._
import org.scalatest.funsuite.AnyFunSuite

final class LokalStringContextTest extends AnyFunSuite {
  test("allows to nest Translations") {
    val hello = de_DE"Hallo"
    val world = de_DE"Welt"
    assert(de_DE"$hello $world".translate(Locales.de_DE) eqv Some("Hallo Welt"))
  }
}
