package io.taig.babel

final case class Dictionary(values: Segments[Text]) extends AnyVal {
  @inline
  def get(path: Path): Option[Text] = values.getLeaf(path)

  def apply(path: Path, quantity: Quantity, arguments: Seq[Any])(implicit formatter: Formatter): Option[String] =
    get(path).map(_.apply(quantity, arguments))

  def apply(path: Path, arguments: Seq[Any])(implicit formatter: Formatter): Option[String] =
    apply(path, Quantity.One, arguments)

  def apply(path: Path, quantity: Quantity): Option[String] = get(path).map(_.apply(quantity))

  def apply(path: Path): Option[String] = apply(path, Quantity.One)

  def toI18n(locale: Locale): Babel =
    Babel(values.mapWithPath((path, text) => Translation.of(path.printPlaceholder)(locale -> text)))

  def toI18nUniversals: Babel = Babel(values.map(Translation.universal))
}

object Dictionary {
  val Empty: Dictionary = Dictionary(Segments.Empty)
}
