package net.slozzer.babel

final case class Dictionary[A](translations: Translations[A], fallback: (Locale, A)) {
  def get(locale: Locale): (Locale, A) =
    translations
      .get(locale)
      .map((locale, _))
      .orElse(translations.get(locale.withoutCountry).map((locale.withoutCountry, _)))
      .getOrElse(fallback)

  def apply(locale: Locale): A = get(locale)._2
}

object Dictionary {
  def of[A](fallback: (Locale, A), translations: (Locale, A)*): Dictionary[A] =
    Dictionary(Translations.from(translations), fallback)
}
