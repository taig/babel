package io.taig.lokal

import scala.collection.immutable.Map

final case class Dictionary(values: Map[Path, Text]) extends AnyVal {
  @inline
  def get(path: Path): Option[Text] = values.get(path)

  def toI18n(locale: Locale): I18n = I18n(values.view.mapValues(Translation.one(locale, _)).toMap)
}
