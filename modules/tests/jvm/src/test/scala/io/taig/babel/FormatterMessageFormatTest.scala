package io.taig.babel

import io.taig.babel.mf._
import munit.FunSuite

final class FormatterMessageFormatTest extends FunSuite {
  test("arguments") {
    assertEquals(obtained = Text.one("{0} / {1} = {2}")(Seq(10, 5, "2")), expected = "10 / 5 = 2")
  }
}
