package io.taig.lokal

import cats.implicits._
import io.taig.lokal.implicits._
import org.scalatest.funsuite.AnyFunSuite

final class TranslationTest extends AnyFunSuite {
  val english: Translation[String] = en"Hello"
  val germany: Translation[String] = de"Hallo"
  val austria: Translation[String] = de_AT"Grüß Gott"
  val spain: Translation[String] = es_ES"Hola"
  val translations: Translation[String] = english & germany & austria & spain

  test("translate picks the exact match") {
    assert(translations.translate(Locale.de_AT) eqv Some("Grüß Gott"))
  }

  test("translate falls back to the language if country is not available") {
    assert(translations.translate(Locale.de_DE) eqv Some("Hallo"))
  }

  test("translate falls back to a different country if all else fails") {
    assert(translations.translate(Locale.es_AR) eqv Some("Hola"))
  }

  test("translate returns None if no matching language is available") {
    assert(translations.translate(Locale.fr) eqv None)
  }
}
