package io.taig.lokal

import scala.collection.immutable.Map

final case class Dictionary(values: Map[Path, Text]) extends AnyVal {
  @inline
  def get(path: Path): Option[Text] = values.get(path)

  def apply(path: Path, quantity: Quantity, arguments: Seq[Any])(implicit formatter: Formatter): Option[String] =
    get(path).map(_.apply(quantity, arguments))

  def apply(path: Path, arguments: Seq[Any])(implicit formatter: Formatter): Option[String] =
    apply(path, Quantity.One, arguments)

  def apply(path: Path, quantity: Quantity): Option[String] = get(path).map(_.apply(quantity))

  def apply(path: Path): Option[String] = apply(path, Quantity.One)

  def toI18n(locale: Locale): I18n = I18n(values.view.mapValues(Translation.one(locale, _)).toMap)
}
