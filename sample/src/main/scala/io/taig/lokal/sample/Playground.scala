package io.taig.lokal.sample

import io.taig.lokal._
import io.taig.lokal.dsl._
import io.taig.lokal.circe._
import io.circe.syntax._
import io.taig.lokal.util.QuantityEncoder

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

//    val nested = Translation(german, Translation.arguments[Translation[Any, String], String]("Nest: %s"))

    println(translations.translate(german, ((), Quantity(0))))
    println(translations.translate(english, ((), Quantity(1))))
    println(translations.translate(english, ((), Quantity(2))))
    println(Translations(Translations.App(Translation.Provide(Quantity(1), translations))).asJson)
//    println(nested.translate(german, translations.contramap[Any](_ => (42, 42))))
  }
}
