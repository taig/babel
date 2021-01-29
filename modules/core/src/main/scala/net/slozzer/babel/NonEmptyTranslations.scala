package net.slozzer.babel

/** A non-empty version of `Translations` that has a fallback translation */
final case class NonEmptyTranslations[A](translations: Translations[A], fallback: (Locale, A)) {
  def get(locale: Locale): (Locale, A) =
    translations
      .get(locale)
      .map((locale, _))
      .orElse(translations.get(locale.withoutCountry).map((locale.withoutCountry, _)))
      .getOrElse(fallback)

  def apply(locale: Locale): A = get(locale)._2

  def map[B](f: A => B): NonEmptyTranslations[B] =
    NonEmptyTranslations(translations.map(f), (fallback._1, f(fallback._2)))

  def mapWithLocale[B](f: (Locale, A) => B): NonEmptyTranslations[B] =
    NonEmptyTranslations(translations.mapWithLocale(f), (fallback._1, f(fallback._1, fallback._2)))

  def ++[B >: A](dictionary: NonEmptyTranslations[B]): NonEmptyTranslations[B] =
    NonEmptyTranslations(translations ++ dictionary.translations, dictionary.fallback)

  def +[B >: A](value: (Locale, B)): NonEmptyTranslations[B] =
    if (fallback._1 == value._1) copy(fallback = value) else copy(translations = translations + value)

  def toTranslations: Translations[A] = translations + fallback

  def locales: Set[Locale] = translations.locales + fallback._1

  def toMap: Map[Locale, A] = translations.toMap + fallback
}

object NonEmptyTranslations {
  def from[A](fallback: (Locale, A), translations: Iterable[(Locale, A)]): NonEmptyTranslations[A] =
    NonEmptyTranslations(Translations.from(translations), fallback)

  def of[A](fallback: (Locale, A), translations: (Locale, A)*): NonEmptyTranslations[A] =
    NonEmptyTranslations.from(fallback, translations)
}
