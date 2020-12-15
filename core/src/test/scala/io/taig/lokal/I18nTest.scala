package io.taig.lokal

import munit.FunSuite

final class I18nTest extends FunSuite {
  test("merge") {
    val left = I18n(Map("x" -> Translation.one(Locales.de, Text.one("foo"))))
    val right = I18n(Map("x" -> Translation.one(Locales.en, Text.one("bar"))))
    val i18n = left merge right
    assertEquals(obtained = i18n.get("x", Locales.de), expected = Some(Text.one("foo")))
    assertEquals(obtained = i18n.get("x", Locales.en), expected = Some(Text.one("bar")))
    assertEquals(obtained = i18n.get("x", Locales.es), expected = None)
  }
}
