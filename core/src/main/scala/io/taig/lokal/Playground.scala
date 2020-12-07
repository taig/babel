package io.taig.lokal

object Playground {
  def main(arguments: Array[String]): Unit = {
    val german = Locale(Language("de"), None)
    val english = Locale(Language("en"), None)
    val translations =
      Translation.via[(Int, Int), String](german, "Hallo welt: %s %d") ~ Translation.universal("Na klaro")

    println(translations(german, (42, 11)))
    println(translations(english, (42, 11)))
  }
}
