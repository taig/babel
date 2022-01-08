package io.taig.babel

import munit.FunSuite

final class QuantityTest extends FunSuite {
  test("parse") {
    assertEquals(obtained = Quantity.parse("0"), expected = Right(Quantity.exact(0)))
    assertEquals(obtained = Quantity.parse("1"), expected = Right(Quantity.exact(1)))
    assertEquals(obtained = Quantity.parse("-1"), expected = Right(Quantity.exact(-1)))
    assertEquals(obtained = Quantity.parse("foobar"), expected = Left("Invalid quantity"))

    assertEquals(obtained = Quantity.parse("0-1"), expected = Right(Quantity.unsafeRange(0, 1)))
    assertEquals(obtained = Quantity.parse("0-0"), expected = Right(Quantity.exact(0)))
    assertEquals(obtained = Quantity.parse("-10-0"), expected = Right(Quantity.unsafeRange(-10, 0)))
    assertEquals(obtained = Quantity.parse("-10--5"), expected = Right(Quantity.unsafeRange(-10, -5)))
    assertEquals(obtained = Quantity.parse("1-2-3"), expected = Left("Invalid quantity"))
  }
}
