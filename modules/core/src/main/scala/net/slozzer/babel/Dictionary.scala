package net.slozzer.babel

final case class Dictionary[A](translations: Translations[A], fallback: (Locale, A)) {
  def get(locale: Locale): (Locale, A) =
    translations
      .get(locale)
      .map((locale, _))
      .orElse(translations.get(locale.withoutCountry).map((locale.withoutCountry, _)))
      .getOrElse(fallback)

  def apply(locale: Locale): A = get(locale)._2

  def map[B](f: A => B): Dictionary[B] = Dictionary(translations.map(f), (fallback._1, f(fallback._2)))

  def mapWithLocale[B](f: (Locale, A) => B): Dictionary[B] =
    Dictionary(translations.mapWithLocale(f), (fallback._1, f(fallback._1, fallback._2)))

  def ++[B >: A](dictionary: Dictionary[B]): Dictionary[B] =
    Dictionary(translations ++ dictionary.translations, dictionary.fallback)

  def +[B >: A](value: (Locale, B)): Dictionary[B] =
    if (fallback._1 == value._1) copy(fallback = value) else copy(translations = translations + value)

  def toTranslations: Translations[A] = translations + fallback

  def locales: Set[Locale] = translations.locales + fallback._1
}

object Dictionary {
  def of[A](fallback: (Locale, A), translations: (Locale, A)*): Dictionary[A] =
    Dictionary(Translations.from(translations), fallback)
}
