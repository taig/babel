package io.taig.lokal.dsl

import cats.syntax.all._
import org.scalatest.funsuite.AnyFunSuite

final class LokalStringContextTest extends AnyFunSuite {
  test("allows to nest Translations") {
    val hello = de_DE"Hallo"
    val world = de_DE"Welt"
    assert(de_DE"$hello $world".translate(Locales.de_DE) eqv Some("Hallo Welt"))
  }

  test("allows to nest Dictionaries") {
    val hello = x"Hello" & de"Hallo"
    val world = x"World" & de"Welt"
    assert(de"$hello $world".translate(Locales.de_DE) eqv Some("Hallo Welt"))
    assert(fr"$hello $world".translate(Locales.fr) eqv Some("Hello World"))
  }
}
