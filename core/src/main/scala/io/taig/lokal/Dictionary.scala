package io.taig.lokal

import scala.collection.immutable.Map

/** A `Dictionary` is a lookup table for `Text` in a specific language, but you probably knew this already */
final case class Dictionary(values: Map[String, Text]) extends AnyVal {
  @inline
  def get(key: String): Option[Text] = values.get(key)

  def apply(key: String, quantity: Quantity): String = get(key).map(_.apply(quantity)).getOrElse(key)

  def apply(key: String): String = apply(key, Quantity.One)

  def toI18n(locale: Locale): I18n = I18n(values.view.mapValues(Translation.one(locale, _)).toMap)
}
