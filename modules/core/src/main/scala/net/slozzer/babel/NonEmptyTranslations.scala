package net.slozzer.babel

/** A non-empty version of `Translations` that has a default translation which is used as a fallthrough fallback */
final case class NonEmptyTranslations[+A] private (default: Translation[A], translations: Translations[A]) {
  def get(locale: Locale): Translation[A] = translations.get(locale).getOrElse(default)

  def apply(locale: Locale): A = get(locale).value

  def map[B](f: A => B): NonEmptyTranslations[B] = NonEmptyTranslations(default.map(f), translations.map(f))

  def mapWithLocale[B](f: (Locale, A) => B): NonEmptyTranslations[B] =
    NonEmptyTranslations(default.mapWithLocale(f), translations.mapWithLocale(f))

  def concat[B >: A](translations: Translations[B]): NonEmptyTranslations[B] =
    NonEmptyTranslations(default, this.translations concat translations)

  def concatNet[B >: A](translations: NonEmptyTranslations[B]): NonEmptyTranslations[B] =
    NonEmptyTranslations(default, this.translations concat translations.toTranslations)

  def +[B >: A](translation: Translation[B]): NonEmptyTranslations[B] =
    if (default.locale == translation.locale) copy(default = translation)
    else copy(translations = translations + translation)

  def toTranslations: Translations[A] = translations + default

  def locales: Set[Locale] = translations.locales + default.locale

  def toMap: Map[Locale, A] = translations.toMap + default.toTuple

  def toList: List[Translation[A]] = toMap.map((Translation[A] _).tupled).toList
}

object NonEmptyTranslations {
  def apply[A](default: Translation[A], translations: Translations[A]): NonEmptyTranslations[A] =
    new NonEmptyTranslations[A](
      translations.toMap.get(default.locale).map(default.as).getOrElse(default),
      translations - default.locale
    )

  def from[A](default: Translation[A], translations: Iterable[Translation[A]]): NonEmptyTranslations[A] =
    NonEmptyTranslations(default, Translations.from(translations))

  def of[A](default: Translation[A], translations: Translation[A]*): NonEmptyTranslations[A] =
    NonEmptyTranslations.from(default, translations)
}
