package net.slozzer.babel

import munit.FunSuite

final class LanguageTest extends FunSuite {
  test("parse") {
    assertEquals(obtained = Language.parse("de"), expected = Some(Language("de")))
    assertEquals(obtained = Language.parse("en"), expected = Some(Language("en")))
    assertEquals(obtained = Language.parse("DE"), expected = None)
    assertEquals(obtained = Language.parse("d"), expected = None)
    assertEquals(obtained = Language.parse("deu"), expected = None)
  }
}
