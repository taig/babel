package io.taig.lokal.sample

import io.circe.syntax._
import io.taig.lokal._
import io.taig.lokal.circe._

object Playground {
  def main(arguments: Array[String]): Unit = {

    val i18n = I18n(
      Map(
        Path.from("hello/world") -> Translation(
          Map(
            Locales.de -> Text("Hallo Welten", Map(Quantity.One -> "Hallo Welt")),
            Locales.de_AT -> Text("Grüß Gott", Map.empty)
          ),
          fallback = None
        )
      )
    )

    println(i18n.asJson)
    println(i18n.asJson.as[I18n])
  }
}
