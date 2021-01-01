package net.slozzer.babel

import munit.FunSuite

import java.util.{Locale => JLocale}

final class LocaleJvmTest extends FunSuite {
  test("fromJavaLocale / toJavaLocale should round trip") {
    JLocale.getAvailableLocales.toList
      .map(locale => Locale.fromJavaLocale(locale).map((locale, _)))
      .collect { case Some(value) => value }
      .foreach { case (jLocale, locale) =>
        assertEquals(obtained = jLocale.getLanguage, expected = locale.toJavaLocale.getLanguage)
        assertEquals(obtained = jLocale.getCountry, expected = locale.toJavaLocale.getCountry)
      }
  }
}
