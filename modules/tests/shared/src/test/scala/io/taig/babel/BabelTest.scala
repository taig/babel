package io.taig.babel

import munit.FunSuite

final class BabelTest extends FunSuite {
  test("merge") {
    val left = Babel(Segments.one("x", Translation.of("fallback1")(Locales.de -> Text.one("foo"))))
    val right = Babel(Segments.one("x", Translation.of("fallback2")(Locales.en -> Text.one("bar"))))
    val i18n = (left merge right).toOption.get
    assertEquals(obtained = i18n(Path.one("x"), Locales.de), expected = "foo")
    assertEquals(obtained = i18n(Path.one("x"), Locales.en), expected = "bar")
    assertEquals(obtained = i18n(Path.one("x"), Locales.es), expected = "fallback2")
  }
}
