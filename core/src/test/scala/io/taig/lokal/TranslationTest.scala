package io.taig.lokal

import cats.syntax.all._
import org.scalatest.funsuite.AnyFunSuite

final class TranslationTest extends AnyFunSuite {
  val translations: Translation[String] = Translation.of(
    List(
      Locale(Language("de")) -> "Hallo",
      Locale(Language("de"), Country("AT")) -> "Grüß Gott",
      Locale(Language("es"), Country("ES")) -> "Hola"
    )
  )

  test("translate picks the exact match") {
    assert(translations.translate(Locale(Language("de"), Country("AT"))) eqv Some("Grüß Gott"))
  }

  test("translate falls back to the language if country is not available") {
    assert(translations.translate(Locale(Language("de"), Country("DE"))) eqv Some("Hallo"))
  }

  test("translate returns None if no matching language is available") {
    assert(translations.translate(Locale(Language("fr"))) eqv None)
  }

//  test("flatMap allows to chain Translations") {
//    val translation = Translation.universal("1.23").flatMap { value =>
//      Translation.one(Locale(Language("de")), value.replace(".", ","), value)
//    }
//
//    assert(translation.translate(Locale(Language("de"))) eqv "1,23")
//    assert(translation.translate(Locale(Language("en"))) eqv "1.23")
//  }
}
