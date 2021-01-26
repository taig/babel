package net.slozzer.babel

final case class Dictionary[A](translations: Translations[A], fallback: A) {
  def apply(locale: Locale): A =
    translations.get(locale).orElse(translations.get(locale.withoutCountry)).getOrElse(fallback)
}

object Dictionary {
  def of[A](fallback: A, translations: (Locale, A)*): Dictionary[A] =
    Dictionary(Translations.from(translations), fallback)
}
