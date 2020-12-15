package io.taig.lokal

import munit.FunSuite

final class TranslationTest extends FunSuite {
  test("prefers direct Locale matches") {
    val translation = Translation(Map(Locales.de_DE -> Text.one("foo")), fallback = None)
    assertEquals(obtained = translation.get(Locales.de_DE), expected = Some(Text.one("foo")))
  }

  test("prefers language fallbacks over general fallbacks") {
    val translation = Translation(Map(Locales.de -> Text.one("foo")), fallback = Some(Text.one("bar")))
    assertEquals(obtained = translation.get(Locales.de_DE), expected = Some(Text.one("foo")))
  }

  test("uses the general fallback as a last resort") {
    val translation = Translation(Map(Locales.de -> Text.one("foo")), fallback = Some(Text.one("bar")))
    assertEquals(obtained = translation.get(Locales.en), expected = Some(Text.one("bar")))
  }

  test("++ favors the right side") {
    val left = Translation(Map(Locales.de -> Text.one("a")), fallback = Some(Text.one("aaa")))
    val right = Translation(Map(Locales.de -> Text.one("b")), fallback = Some(Text.one("bbb")))
    val translations = left ++ right
    assertEquals(obtained = translations.get(Locales.de), expected = Some(Text.one("b")))
    assertEquals(obtained = translations.get(Locales.en), expected = Some(Text.one("bbb")))
  }
}
