package io.taig.lokal

import munit.FunSuite
import io.taig.lokal.printf._

final class FormatterPrintFTest extends FunSuite {
  test("arguments") {
    assertEquals(obtained = Text.one("%d / %d = %s")(Seq(10, 5, "2")), expected = "10 / 5 = 2")
  }
}
