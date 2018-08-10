package io.taig.lokal

import java.util.Locale

import cats.implicits._
import cats.data.NonEmptyList
import io.taig.lokal.implicits._

case class Translations[A](values: NonEmptyList[Translation[A]])
    extends AnyVal {
  def &(translations: Translations[A]): Translations[A] =
    Translations(values concatNel translations.values)

  def resolve(locale: Locale): Translation[A] =
    values
      .find(_.locale === locale)
      .orElse(values.find(_.locale.getLanguage === locale.getLanguage))
      .orElse(values.find(_.locale === WildcardLocale))
      .getOrElse(values.head)

  def translate(locale: Locale): A = resolve(locale).value
}

object Translations {
  def of[A](translation: Translation[A]): Translations[A] =
    Translations(NonEmptyList.one(translation))

  def of[A](locale: Locale, value: A): Translations[A] =
    Translations.of(Translation(locale, value))
}
