package net.slozzer.babel

import CodecTest._
import munit.FunSuite

final class CodecTest extends FunSuite {
  val deeplyNestedSegments: Segments[Text] = Segments.one("bar", Text.one("z"))

  val segments: Segments[Text] = Segments(
    Map(
      "foo" -> Left(Text.one("x")),
      "nested" -> Right(Segments(Map("foobar" -> Left(Text.one("y")), "deeplyNested" -> Right(deeplyNestedSegments))))
    )
  )

  val deeplyNestedTranslations: MyDeeplyNestedTranslations[Text] =
    MyDeeplyNestedTranslations(bar = Text.one("z"), opt = None)

  val translations: MyTranslations[Text] = MyTranslations(
    foo = Text.one("x"),
    nested = MyNestedTranslations(foobar = Text.one("y"), deeplyNested = deeplyNestedTranslations)
  )

  test("decode") {
    val obtained = DerivedDecoder[Text, MyDeeplyNestedTranslations[Text]].decode(Path.Root, deeplyNestedSegments)
    assertEquals(obtained, expected = Right(deeplyNestedTranslations))
  }

  test("encode") {
    val obtained = DerivedEncoder[MyDeeplyNestedTranslations[Text], Text].encode(deeplyNestedTranslations)
    assertEquals(obtained, expected = deeplyNestedSegments)
  }

  test("semiauto") {
    generic.semiauto.deriveDecoder[MyDeeplyNestedTranslations, Text]
    generic.semiauto.deriveEncoder[MyDeeplyNestedTranslations, Text]

    assertNoDiff(
      compileErrors("generic.semiauto.deriveDecoder[MyTranslations, Text]"),
      """|error: could not find implicit value for parameter decoder: net.slozzer.babel.DerivedDecoder[net.slozzer.babel.Text,net.slozzer.babel.CodecTest.MyTranslations[net.slozzer.babel.Text]]
         |generic.semiauto.deriveDecoder[MyTranslations, Text]
         |                              ^
         |""".stripMargin
    )

    assertNoDiff(
      compileErrors("generic.semiauto.deriveEncoder[MyTranslations, Text]"),
      """|error: could not find implicit value for parameter encoder: net.slozzer.babel.DerivedEncoder[net.slozzer.babel.CodecTest.MyTranslations[net.slozzer.babel.Text],net.slozzer.babel.Text]
         |generic.semiauto.deriveEncoder[MyTranslations, Text]
         |                              ^
         |""".stripMargin
    )
  }

  test("auto") {
    import generic.auto._

    Encoder[MyTranslations, Text]
    Decoder[MyTranslations, Text]
  }
}

object CodecTest {
  final case class MyTranslations[A](foo: A, nested: MyNestedTranslations[A])

  final case class MyNestedTranslations[A](foobar: A, deeplyNested: MyDeeplyNestedTranslations[A])

  final case class MyDeeplyNestedTranslations[A](bar: A, opt: Option[A])
}
