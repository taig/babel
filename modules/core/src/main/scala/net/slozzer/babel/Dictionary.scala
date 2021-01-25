package net.slozzer.babel

final case class Dictionary[A](translations: Translations[A], fallback: A) {
  def apply(locale: Locale): A =
    translations.get(locale).orElse(translations.get(locale.withoutCountry)).getOrElse(fallback)
}
