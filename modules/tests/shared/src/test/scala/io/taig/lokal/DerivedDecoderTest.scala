package io.taig.lokal

import io.taig.lokal.DerivedDecoderTest.{MyNestedTranslations, MyTranslations}
import munit.FunSuite

final class DerivedDecoderTest extends FunSuite {
  test("decode") {
    val segments = Segments(
      Map(
        "foo" -> Left(Text.one("x")),
        "bar" -> Left(Text.one("y")),
        "nested" -> Right(Segments.one("foobar", Text.one("z")))
      )
    )

    val obtained = DerivedDecoder[Text, MyTranslations[Text]].decode(segments)

    val expected = MyTranslations(
      foo = Text.one("x"),
      bar = Text.one("y"),
      nested = MyNestedTranslations(foobar = Text.one("z"))
    )

    assertEquals(obtained, Right(expected))
  }
}

object DerivedDecoderTest {
  final case class MyTranslations[A](foo: A, bar: A, nested: MyNestedTranslations[A])

  final case class MyNestedTranslations[A](foobar: A)
}
