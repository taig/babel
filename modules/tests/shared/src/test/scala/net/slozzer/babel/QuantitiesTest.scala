package net.slozzer.babel

import munit.FunSuite

final class QuantitiesTest extends FunSuite {
  test("apply prefers matching Quantity.Exact") {
    val text = Quantities.of("foo", Quantities.Element(Quantity.One, "bar"))
    assertEquals(obtained = text(quantity = 1), expected = "bar")
  }

  test("apply uses fallback when no Quantity matches") {
    val text = Quantities.of("foo", Quantities.Element(Quantity.One, "bar"))
    assertEquals(obtained = text(quantity = 0), expected = "foo")
  }

  test("apply handles ranges") {
    val text = Quantities.of("foo", Quantities.Element(Quantity.unsafeRange(10, 13), "bar"))
    assertEquals(obtained = text(quantity = 9), expected = "foo")
    assertEquals(obtained = text(quantity = 10), expected = "bar")
    assertEquals(obtained = text(quantity = 11), expected = "bar")
    assertEquals(obtained = text(quantity = 13), expected = "bar")
    assertEquals(obtained = text(quantity = 14), expected = "foo")
  }
}
