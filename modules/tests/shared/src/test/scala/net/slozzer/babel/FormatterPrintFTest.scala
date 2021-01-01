package net.slozzer.babel

import printf._
import munit.FunSuite

final class FormatterPrintFTest extends FunSuite {
  test("arguments") {
    assertEquals(obtained = Text.one("%d / %d = %s")(Seq(10, 5, "2")), expected = "10 / 5 = 2")
  }
}
