package io.taig.lokal

import org.scalacheck.Gen
import cats.implicits._

object Generators {
  val locales: List[Locale] = List(
    Locale(Language("de")),
    Locale(Language("de"), Country("AT")),
    Locale(Language("de"), Country("CH")),
    Locale(Language("de"), Country("DE")),
    Locale(Language("en")),
    Locale(Language("en"), Country("GB")),
    Locale(Language("en"), Country("US")),
    Locale(Language("es")),
    Locale(Language("fr"))
  )

  val locale: Gen[Locale] = Gen.oneOf(locales)

  def translationOne[A](value: Gen[A]): Gen[Translation[A]] =
    for {
      locale <- locale
      translation <- value
    } yield Translation.one(locale, translation)

  def translationUniversal[A](value: Gen[A]): Gen[Translation[A]] =
    value.map(Translation.universal)

  def translation[A](value: Gen[A]): Gen[Translation[A]] =
    Gen.oneOf(translationOne(value), translationUniversal(value))

  def translations[A](value: Gen[A]): Gen[Translation[A]] =
    Gen.listOf(translation(value)).map(_.combineAll(Translation.monoidK.algebra))

  def dictionaryOne[A](value: Gen[A]): Gen[Dictionary[A]] =
    for {
      locale <- locale
      translation <- value
      fallback <- value
    } yield Dictionary.one(locale, translation, fallback)

  def dictionaryUniversal[A](value: Gen[A]): Gen[Dictionary[A]] =
    value.map(Dictionary.universal)

  def dictionary[A](value: Gen[A]): Gen[Dictionary[A]] =
    Gen.oneOf(dictionaryOne(value), dictionaryUniversal(value))

  def dictionaries[A](value: Gen[A]): Gen[Dictionary[A]] =
    for {
      dictionary <- dictionary(value)
      translations <- Gen.listOf(translation(value))
    } yield translations.foldLeft(dictionary)(_ & _)
}
