package io.taig.lokal

import munit.FunSuite

final class TextTest extends FunSuite {
  test("apply prefers matching Quantities") {
    val text = Text("foo", quantities = Map(Quantity.One -> "bar"))
    assertEquals(obtained = text.apply(Quantity.One), expected = "bar")
  }

  test("apply uses fallback when no Quantity matches") {
    val text = Text("foo", quantities = Map(Quantity.One -> "bar"))
    assertEquals(obtained = text.apply(Quantity.Zero), expected = "foo")
  }
}
