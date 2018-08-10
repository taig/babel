package io.taig.lokal.syntax

import java.util.Locale

import io.taig.lokal._
import io.taig.lokal.LokalStringOperations

final class LokalStringContext(context: StringContext)
    extends LokalStringOperations {
  private def substitute(locale: Locale, arguments: Seq[Any]): String = {
    val translations = arguments.map {
      case translations @ Translations(_) ⇒ translations.translate(locale)
      case default ⇒ default
    }

    context.s(translations: _*)
  }

  protected def apply(locale: Locale,
                      arguments: Seq[Any]): Translations[String] =
    Translations.of(locale, substitute(locale, arguments))

  def all(arguments: Any*): Translations[String] =
    apply(WildcardLocale, arguments)
}
