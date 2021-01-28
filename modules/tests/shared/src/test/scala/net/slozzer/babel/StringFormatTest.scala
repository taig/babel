package net.slozzer.babel

import munit.FunSuite

final class StringFormatTest extends FunSuite {
  test("parse") {
    assertEquals(obtained = StringFormat.parse(""), expected = StringFormat.of(""))
    assertEquals(obtained = StringFormat.parse("foo"), expected = StringFormat.of("foo"))
    assertEquals(obtained = StringFormat.parse("foo {0} bar"), expected = StringFormat.of("foo ", 0 -> " bar"))
    assertEquals(
      obtained = StringFormat.parse("foo {0} bar {1} baz"),
      expected = StringFormat.of("foo ", 0 -> " bar ", 1 -> " baz")
    )
    assertEquals(
      obtained = StringFormat.parse("foo {0} bar {313} baz"),
      expected = StringFormat.of("foo ", 0 -> " bar ", 313 -> " baz")
    )
    assertEquals(
      obtained = StringFormat.parse("foo {0} bar {0} baz"),
      expected = StringFormat.of("foo ", 0 -> " bar ", 0 -> " baz")
    )
    assertEquals(obtained = StringFormat.parse("{0} bar"), expected = StringFormat.of("", 0 -> " bar"))
  }

  test("decoder") {
    val Right(format) = Decoder[StringFormat2].decode(Babel.Value("lorem {1} dolar {0} amet"))
    assertEquals(obtained = format("sit", "ipsum"), "lorem ipsum dolar sit amet")
  }

  test("decoder has too many placeholders") {
    assert(Decoder[StringFormat1].decode(Babel.Value("lorem {1} dolar {0} amet")).isLeft)
  }

  test("decoder has invalid indices") {
    assert(Decoder[StringFormat1].decode(Babel.Value("lorem {1} dolar")).isLeft)
  }

  test("decoder has deuplicate placeholders") {
    assert(Decoder[StringFormat2].decode(Babel.Value("lorem {0} dolar {0} amet")).isLeft)
  }
}
