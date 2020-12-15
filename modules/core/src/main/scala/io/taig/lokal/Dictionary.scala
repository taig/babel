package io.taig.lokal

import scala.collection.immutable.Map

/** A `Dictionary` is a lookup table for `Text` in a specific language, but you probably knew this already */
final case class Dictionary(values: Map[Path, Text]) extends AnyVal {
  @inline
  def get(path: Path): Option[Text] = values.get(path)

  def toI18n(locale: Locale): I18n = I18n(values.view.mapValues(Translation.one(locale, _)).toMap)
}
