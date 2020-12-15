package io.taig.lokal

final case class Dictionary(values: Segments[Text]) extends AnyVal {
  @inline
  def get(path: Path): Option[Text] = values.findLeaf(path)

  def apply(path: Path, quantity: Quantity, arguments: Seq[Any])(implicit formatter: Formatter): Option[String] =
    get(path).map(_.apply(quantity, arguments))

  def apply(path: Path, arguments: Seq[Any])(implicit formatter: Formatter): Option[String] =
    apply(path, Quantity.One, arguments)

  def apply(path: Path, quantity: Quantity): Option[String] = get(path).map(_.apply(quantity))

  def apply(path: Path): Option[String] = apply(path, Quantity.One)

  def toI18n(locale: Locale): I18n = I18n(values.map(Translation.one(locale, _)))

  def toI18nFallbacks: I18n = I18n(values.map(Translation.fallback))
}

object Dictionary {
  val Empty: Dictionary = Dictionary(Segments.Empty)
}
