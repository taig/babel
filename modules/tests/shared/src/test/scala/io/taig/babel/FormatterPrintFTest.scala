package io.taig.babel

import io.taig.babel.printf._
import munit.FunSuite

final class FormatterPrintFTest extends FunSuite {
  test("arguments") {
    assertEquals(obtained = Text.one("%d / %d = %s")(Seq(10, 5, "2")), expected = "10 / 5 = 2")
  }
}
