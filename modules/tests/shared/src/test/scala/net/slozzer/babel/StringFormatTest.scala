package net.slozzer.babel

import munit.FunSuite

final class StringFormatTest extends FunSuite {
  test("parse") {
    assertEquals(obtained = StringFormat.parse(""), expected = StringFormat.of(""))
    assertEquals(obtained = StringFormat.parse("foo"), expected = StringFormat.of("foo"))
    assertEquals(obtained = StringFormat.parse("foo $1 bar"), expected = StringFormat.of("foo ", 1 -> " bar"))
    assertEquals(
      obtained = StringFormat.parse("foo $1 bar $2 baz"),
      expected = StringFormat.of("foo ", 1 -> " bar ", 2 -> " baz")
    )
    assertEquals(
      obtained = StringFormat.parse("foo $1 bar $313 baz"),
      expected = StringFormat.of("foo ", 1 -> " bar ", 313 -> " baz")
    )
    assertEquals(
      obtained = StringFormat.parse("foo $1 bar $1 baz"),
      expected = StringFormat.of("foo ", 1 -> " bar ", 1 -> " baz")
    )
    assertEquals(obtained = StringFormat.parse("$1 bar"), expected = StringFormat.of("", 1 -> " bar"))
  }
}
