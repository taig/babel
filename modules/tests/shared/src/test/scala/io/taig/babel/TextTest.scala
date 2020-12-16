package io.taig.babel

import munit.FunSuite

final class TextTest extends FunSuite {
  test("apply prefers matching Quantities") {
    val text = Text("foo", quantities = Map(Quantity.One -> "bar"))
    assertEquals(obtained = text(quantity = 1), expected = "bar")
  }

  test("apply uses fallback when no Quantity matches") {
    val text = Text("foo", quantities = Map(Quantity.One -> "bar"))
    assertEquals(obtained = text(quantity = 0), expected = "foo")
  }
}
