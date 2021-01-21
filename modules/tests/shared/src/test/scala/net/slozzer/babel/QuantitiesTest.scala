package net.slozzer.babel

import munit.FunSuite

final class QuantitiesTest extends FunSuite {
  test("apply prefers matching Quantities") {
    val text = Quantities("foo", quantities = Map(Quantity.One -> "bar"))
    assertEquals(obtained = text(quantity = 1), expected = "bar")
  }

  test("apply uses fallback when no Quantity matches") {
    val text = Quantities("foo", quantities = Map(Quantity.One -> "bar"))
    assertEquals(obtained = text(quantity = 0), expected = "foo")
  }
}
