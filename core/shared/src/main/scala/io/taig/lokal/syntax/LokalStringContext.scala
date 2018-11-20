package io.taig.lokal.syntax

import java.util.Locale

import io.taig.lokal._
import io.taig.lokal.LokalStringOperations

final class LokalStringContext(context: StringContext)
    extends LokalStringOperations {
  private def substitute(locale: Locale, arguments: Seq[Any]): String = {
    val translations = arguments.map {
      case translation: Translation[_] ⇒ translation(locale).toString
      case value ⇒ value.toString
    }

    context.s(translations: _*)
  }

  protected def apply(locale: Locale,
                      arguments: Seq[Any]): Translation[String] =
    Translation(locale, substitute(locale, arguments))
}
