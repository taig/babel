package io.taig.lokal.sample

import io.circe.Json
import io.taig.lokal._
import io.taig.lokal.dsl._
import io.taig.lokal.circe._
import io.circe.syntax._
import io.taig.lokal.util.{Decoder, Encoder, QuantityEncoder}
import shapeless._
import cats.implicits._

object Playground {
  def main(arguments: Array[String]): Unit = {
    val german = Locale(Language("de"), None)
    val english = Locale(Language("en"), None)
    val spanish = Locale(Language("es"), None)

    val translations: Translation[(Any, Quantity), String] =
      Translation.Plurals(
        QuantityEncoder.string,
        "n",
        Translation.universal("foobar %n"),
        Map(Quantity.One -> Translation.universal("foo"), Quantity.Zero -> Translation.universal("bar"))
      )

    final case class Test(inner: Inner)

    final case class Inner(yolo: Dictionary)

    println(Encoder[Test].apply(Test(Inner(Translation.universal("hhihi")))))

    println(
      Decoder[Test].apply(
        Mode.Monoglot(german),
        strict = true,
        Json.obj("inner" := Json.obj("yolo" := "hallo")).noSpaces
      )
    )

//    println(translations.translate(german, ((), Quantity(0))))
//    println(translations.translate(english, ((), Quantity(1))))
//    println(translations.translate(english, ((), Quantity(2))))
//    println(Translations(Translations.App(Translation.Provide(Quantity(1), translations))).asJson)
//    println(nested.translate(german, translations.contramap[Any](_ => (42, 42))))
  }
}
