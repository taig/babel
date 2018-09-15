package io.taig.lokal

import java.util.Locale

import cats.implicits._
import cats.data.NonEmptyList
import cats.Eq
import io.taig.lokal.implicits._

case class Translations[A](values: NonEmptyList[Translation[A]])
    extends AnyVal {
  def &(translations: Translations[A]): Translations[A] =
    Translations(values concatNel translations.values)

  /**
    * Find the [[io.taig.lokal.Translation]] that matches the given `Locale`
    * best
    *
    * Matches are considered in the following order:
    *
    * <ol>
    *   <li>direct matches with equal language & country</li>
    *   <li>matches with equal language</li>
    *   <li>wildcard translation</li>
    * </ol>
    */
  def tryResolve(locale: Locale): Option[Translation[A]] =
    values
      .find(_.locale === locale)
      .orElse(values.find(_.locale.getLanguage === locale.getLanguage))
      .orElse(values.find(_.locale === WildcardLocale))

  /**
    * Find the [[io.taig.lokal.Translation]] that matches the given `Locale`
    * best
    *
    * Matches are considered in the following order:
    *
    * <ol>
    *   <li>direct matches with equal language & country</li>
    *   <li>matches with equal language</li>
    *   <li>wildcard translation</li>
    *   <li>the first available translation</li>
    * </ol>
    */
  def resolve(locale: Locale): Translation[A] =
    tryResolve(locale).getOrElse(values.head)

  /**
    * Find the [[io.taig.lokal.Translation]] that matches the given `Locale`
    * best and return its `value`
    *
    * @see resolve
    */
  def translate(locale: Locale): A = resolve(locale).value
}

object Translations {
  def of[A](translation: Translation[A]): Translations[A] =
    Translations(NonEmptyList.one(translation))

  def of[A](locale: Locale, value: A): Translations[A] =
    Translations.of(Translation(locale, value))

  implicit def eq[A: Eq]: Eq[Translations[A]] = Eq.by(_.values)
}
