package io.taig.lokal

import java.util.{Locale => JLocale}
import cats.syntax.all._

import org.scalatest.funsuite.AnyFunSuite

final class LocaleJvmTest extends AnyFunSuite {
  test("fromJavaLocale / toJavaLocale should round trip") {
    JLocale.getAvailableLocales.toList
      .mapFilter(locale => Locale.fromJavaLocale(locale).tupleLeft(locale))
      .foreach {
        case (jLocale, locale) =>
          assert(jLocale.getLanguage == locale.toJavaLocale.getLanguage)
          assert(jLocale.getCountry == locale.toJavaLocale.getCountry)
      }
  }
}
