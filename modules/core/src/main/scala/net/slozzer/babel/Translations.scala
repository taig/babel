package net.slozzer.babel

/** A `Map`-like structure with `Locale`-keys */
final case class Translations[+A](values: Map[Locale, A]) extends AnyVal {
  def get(locale: Locale): Option[A] = values.get(locale)

  def map[B](f: A => B): Translations[B] = Translations(values.map { case (locale, value) => (locale, f(value)) })

  def mapWithLocale[B](f: (Locale, A) => B): Translations[B] =
    Translations(values.map { case (locale, value) => (locale, f(locale, value)) })

  def ++[B >: A](translations: Translations[B]): Translations[B] = Translations(values ++ translations.values)

  def +[B >: A](value: (Locale, B)): Translations[B] = Translations(values + value)

  def -(locale: Locale): Translations[A] = Translations(values - locale)

  def locales: Set[Locale] = values.keySet

  /** Convert to `NonEmptyTranslations` using the given `Locale` as the fallback
    *
    * @return `None` if the given `Locale` is not present in this collection, `Some` of `NonEmptyTranslations` otherwise
    */
  def withFallback[B >: A](locale: Locale): Option[NonEmptyTranslations[B]] =
    values.get(locale).map(value => NonEmptyTranslations(this - locale, (locale, value)))

  def toMap: Map[Locale, A] = values
}

object Translations {
  val Empty: Translations[Nothing] = Translations(Map.empty)

  def from[A](values: Iterable[(Locale, A)]): Translations[A] = Translations(values.toMap)

  def of[A](values: (Locale, A)*): Translations[A] = from(values)

  def one[A](locale: Locale, value: A): Translations[A] = Translations(Map(locale -> value))
}
