package net.slozzer.babel

import CodecTest._
import munit.FunSuite

final class CodecTest extends FunSuite {
  val deeplyNestedSegments: Segments[Quantities] = Segments.one("bar", Quantities.one("z"))

  val segments: Segments[Quantities] = Segments(
    Map(
      "foo" -> Left(Quantities.one("x")),
      "nested" -> Right(Segments(Map("foobar" -> Left(Quantities.one("y")), "deeplyNested" -> Right(deeplyNestedSegments))))
    )
  )

  val deeplyNestedTranslations: MyDeeplyNestedTranslations[Quantities] =
    MyDeeplyNestedTranslations(bar = Quantities.one("z"), opt = None)

  val translations: MyTranslations[Quantities] = MyTranslations(
    foo = Quantities.one("x"),
    nested = MyNestedTranslations(foobar = Quantities.one("y"), deeplyNested = deeplyNestedTranslations)
  )

  test("decode") {
    val obtained = DerivedDecoder[Quantities, MyDeeplyNestedTranslations[Quantities]].decode(Path.Root, deeplyNestedSegments)
    assertEquals(obtained, expected = Right(deeplyNestedTranslations))
  }

  test("encode") {
    val obtained = DerivedEncoder[MyDeeplyNestedTranslations[Quantities], Quantities].encode(deeplyNestedTranslations)
    assertEquals(obtained, expected = deeplyNestedSegments)
  }

  test("semiauto") {
    generic.semiauto.deriveDecoder[MyDeeplyNestedTranslations, Quantities]
    generic.semiauto.deriveEncoder[MyDeeplyNestedTranslations, Quantities]

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

    Encoder[MyTranslations, Quantities]
    Decoder[MyTranslations, Quantities]
  }
}

object CodecTest {
  final case class MyTranslations[A](foo: A, nested: MyNestedTranslations[A])

  final case class MyNestedTranslations[A](foobar: A, deeplyNested: MyDeeplyNestedTranslations[A])

  final case class MyDeeplyNestedTranslations[A](bar: A, opt: Option[A])
}
