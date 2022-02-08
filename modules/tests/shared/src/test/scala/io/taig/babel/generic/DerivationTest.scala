package io.taig.babel

import io.taig.babel.generic.auto._
import io.taig.babel._

import munit.FunSuite
import DerivationTest._

final class DerivationTest extends FunSuite {

  test("Roundtrip test derived decoder and encoder") {

    val encoder: Encoder[Foo] = deriveEncoder[Foo]
    val decoder: Decoder[Foo] = deriveDecoder[Foo]

    val foo: Foo = {

      val messages: Quantities[StringFormat1] = {
        val sfOne: StringFormat1 = (v0: String) => StringFormat.build("You clicked ", Map(0 -> " time"), Vector(v0))
        val sfMany: StringFormat1 = (v0: String) => StringFormat.build("You clicked ", Map(0 -> " times"), Vector(v0))
        Quantities.from[StringFormat1](sfMany, List(Quantities.Element(Quantity.One, sfOne)))
      }

      Foo(
        Foo.Bar("stringValue", Some("stringOptValue"), messages),
        "stringValue",
        Some("stringOptValue"),
        messages
      )
    }

    val obtained = decoder.decode(encoder.encode(foo)).getOrElse(fail("dd"))
    val expected = foo

    def assertMessagesEqual(obtained: Quantities[StringFormat1], expected: Quantities[StringFormat1]): Unit = {
      val executeStringFormat: StringFormat1 => String = _("1")
      assertEquals(obtained.map(executeStringFormat), expected.map(executeStringFormat))
    }

    assertEquals(obtained.bar.string, expected.bar.string)
    assertEquals(obtained.bar.stringOpt, expected.bar.stringOpt)
    assertMessagesEqual(obtained.bar.message, expected.bar.message)
    assertEquals(obtained.string, expected.string)
    assertEquals(obtained.stringOpt, expected.stringOpt)
    assertMessagesEqual(obtained.message, expected.message)
  }
}

object DerivationTest {

  final case class Foo(
      bar: Foo.Bar,
      string: String,
      stringOpt: Option[String],
      message: Quantities[StringFormat1]
  )

  object Foo {

    final case class Bar(
        string: String,
        stringOpt: Option[String],
        message: Quantities[StringFormat1]
    )
  }
}
