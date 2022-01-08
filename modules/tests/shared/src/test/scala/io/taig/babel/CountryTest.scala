package io.taig.babel

import munit.FunSuite

final class CountryTest extends FunSuite {
  test("parse") {
    assertEquals(obtained = Country.parse("DE"), expected = Some(Country("DE")))
    assertEquals(obtained = Country.parse("EN"), expected = Some(Country("EN")))
    assertEquals(obtained = Country.parse("de"), expected = None)
    assertEquals(obtained = Country.parse("D"), expected = None)
    assertEquals(obtained = Country.parse("DEU"), expected = None)
  }
}
