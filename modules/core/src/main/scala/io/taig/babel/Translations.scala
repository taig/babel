package io.taig.babel

/** A `Map`-like structure with `Locale`-keys */
final case class Translations[+A] private (values: Map[Locale, A]) extends AnyVal {
  def get(locale: Locale): Option[Translation[A]] =
    values
      .get(locale)
      .map(Translation(locale, _))
      .orElse(values.get(locale.withoutCountry).map(Translation(locale.withoutCountry, _)))

  def apply(locale: Locale): Option[A] = get(locale).map(_.value)

  def map[B](f: A => B): Translations[B] = Translations(values.map { case (locale, value) => (locale, f(value)) })

  def mapWithLocale[B](f: (Locale, A) => B): Translations[B] =
    Translations(values.map { case (locale, value) => (locale, f(locale, value)) })

  def concat[B >: A](translations: Translations[B]): Translations[B] = Translations(values ++ translations.values)

  def +[B >: A](translation: Translation[B]): Translations[B] = Translations(values + translation.toTuple)

  def -(locale: Locale): Translations[A] = Translations(values - locale)

  def locales: Set[Locale] = values.keySet

  /** Convert to `NonEmptyTranslations` using the given `Locale` as the fallback
    *
    * @return
    *   `None` if the given `Locale` is not present in this collection, `Some` of `NonEmptyTranslations` otherwise
    */
  def withFallback[B >: A](locale: Locale): Option[NonEmptyTranslations[B]] =
    get(locale).map(NonEmptyTranslations(_, this - locale))

  def toMap: Map[Locale, A] = values

  def toList: List[Translation[A]] = toMap.map((Translation[A] _).tupled).toList
}

object Translations {
  val Empty: Translations[Nothing] = Translations(Map.empty)

  def apply[A](values: Map[Locale, A]): Translations[A] = new Translations[A](values)

  def from[A](values: Iterable[Translation[A]]): Translations[A] = Translations(values.map(_.toTuple).toMap)

  def of[A](values: Translation[A]*): Translations[A] = from(values)
}
