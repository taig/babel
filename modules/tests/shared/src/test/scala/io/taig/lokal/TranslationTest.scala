package io.taig.lokal

import munit.FunSuite

final class TranslationTest extends FunSuite {
  test("prefers direct Locale matches") {
    val translation = Translation.of("fallback")(Locales.de_DE -> Text.one("foo"))
    assertEquals(obtained = translation(Locales.de_DE), expected = "foo")
  }

  test("prefers language fallbacks over general fallbacks") {
    val translation = Translation.of("fallback")(Locales.de -> Text.one("foo"))
    assertEquals(obtained = translation(Locales.de_DE), expected = "foo")
  }

  test("uses the general fallback if no language matches") {
    val translation = Translation(Map(Locales.de -> Text.one("foo")), fallback = Right(Text.one("bar")))
    assertEquals(obtained = translation(Locales.en), expected = "bar")
  }

  test("uses the emergency fallback as a last resort") {
    val translation = Translation(Map(Locales.de -> Text.one("foo")), fallback = Left("fallback"))
    assertEquals(obtained = translation(Locales.en), expected = "fallback")
  }

  test("++ favors the right side") {
    val left = Translation.of("fallback1")(Locales.de -> Text.one("a"))
    val right = Translation.of("fallback2")(Locales.de -> Text.one("b"))
    val translations = left ++ right
    assertEquals(obtained = translations(Locales.de), expected = "b")
    assertEquals(obtained = translations(Locales.en), expected = "fallback2")
  }
}
