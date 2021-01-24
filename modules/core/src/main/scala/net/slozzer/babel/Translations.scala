package net.slozzer.babel

final case class Translations[+A](values: Map[Option[Locale], A]) extends AnyVal {
  def map[B](f: A => B): Translations[B] = Translations(values.map { case (locale, value) => (locale, f(value)) })

  def ++[B >: A](translations: Translations[B]): Translations[B] = Translations(values ++ translations.values)
}

object Translations {
  val Empty: Translations[Nothing] = Translations(Map.empty)

  def from[A](values: Iterable[(Option[Locale], A)]): Translations[A] = Translations(values.toMap)

  def one[A](locale: Option[Locale], value: A): Translations[A] = Translations(Map(locale -> value))
}
