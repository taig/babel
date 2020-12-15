package io.taig.lokal

import munit.FunSuite
import io.taig.lokal.dsl._

final class DslTest extends FunSuite {
  test("arguments") {
    assertEquals(obtained = Text.one("%d / %d = %s").apply(Seq(10, 5, "2")), expected = "10 / 5 = 2")
  }
}
