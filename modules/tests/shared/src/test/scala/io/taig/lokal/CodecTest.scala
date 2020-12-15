package io.taig.lokal

import io.taig.lokal.CodecTest.{MyNestedTranslations, MyTranslations}
import munit.FunSuite

final class CodecTest extends FunSuite {
  val segments = Segments(
    Map(
      "foo" -> Left(Text.one("x")),
      "bar" -> Left(Text.one("y")),
      "nested" -> Right(Segments.one("foobar", Text.one("z")))
    )
  )

  val translations = MyTranslations(
    foo = Text.one("x"),
    bar = Text.one("y"),
    nested = MyNestedTranslations(foobar = Text.one("z"))
  )

  test("decode") {
    val obtained = DerivedDecoder[Text, MyTranslations[Text]].decode(segments)
    assertEquals(obtained, expected = Right(translations))
  }

  test("encode") {
    val obtained = DerivedEncoder[MyTranslations[Text], Text].encode(translations)
    assertEquals(obtained, expected = segments)
  }

  test("semiauto") {
    generic.semiauto.deriveEncoder[MyTranslations, Text]
    generic.semiauto.deriveDecoder[MyTranslations, Text]
  }

  test("auto") {
    import generic.auto._

    Encoder[MyTranslations, Text]
    Decoder[MyTranslations, Text]
  }
}

object CodecTest {
  final case class MyTranslations[A](foo: A, bar: A, nested: MyNestedTranslations[A])

  final case class MyNestedTranslations[A](foobar: A)
}
