package io.taig.lokal.dsl

import io.taig.lokal.{Language, Locale, Translation}

object Playground {
  def main(arguments: Array[String]): Unit = {
    val german = Locale(Language("de"), None)
    val english = Locale(Language("en"), None)

    val translations: Translation[(Int, Int), String] =
      Translation.apply(german, Translation.arguments[(Int, Int), String]("Hallo welt: %s %d")) ~
        Translation.universal("Na klaro")

    val nested = Translation(german, Translation.arguments[Translation[Any, String], String]("Nest: %s"))

    println(translations.translate(german, (42, 11)))
    println(translations.translate(english, (42, 11)))
    println(nested.translate(german, translations.contramap[Any](_ => (42, 42))))
  }
}
