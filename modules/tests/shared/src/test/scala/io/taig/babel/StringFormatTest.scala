package io.taig.babel

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
    val format = Decoder[StringFormat2].decode(Babel.Value("lorem {1} dolar {0} amet"))
    assertEquals(obtained = format.map(_.apply("sit", "ipsum")), Right("lorem ipsum dolar sit amet"))
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

  test("decoder with 5+ placeholders preserves segment order") {
    val format = Decoder[StringFormat6].decode(
      Babel.Value("a {0} b {1} c {2} d {3} e {4} f {5} g")
    )
    assertEquals(
      obtained = format.map(_.apply("V0", "V1", "V2", "V3", "V4", "V5")),
      expected = Right("a V0 b V1 c V2 d V3 e V4 f V5 g")
    )
  }

  test("decoder with 5+ placeholders out of order") {
    val format = Decoder[StringFormat6].decode(
      Babel.Value("{5} a {3} b {1} c {4} d {0} e {2} f")
    )
    assertEquals(
      obtained = format.map(_.apply("V0", "V1", "V2", "V3", "V4", "V5")),
      expected = Right("V5 a V3 b V1 c V4 d V0 e V2 f")
    )
  }

  test("decoder with 8 placeholders") {
    val format = Decoder[StringFormat8].decode(
      Babel.Value("{0}-{1}-{2}-{3}-{4}-{5}-{6}-{7}")
    )
    assertEquals(
      obtained = format.map(_.apply("a", "b", "c", "d", "e", "f", "g", "h")),
      expected = Right("a-b-c-d-e-f-g-h")
    )
  }
}
