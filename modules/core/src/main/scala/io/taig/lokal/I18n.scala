package io.taig.lokal

final case class I18n(values: Segments[Translation]) extends AnyVal {
  @inline
  def get(path: Path): Option[Translation] = values.findLeaf(path)

  def apply(path: Path, locale: Locale, quantity: Quantity, arguments: Seq[Any])(
      implicit formatter: Formatter
  ): String =
    get(path).map(_.apply(locale, quantity, arguments)).getOrElse(path.printPretty)

  def apply(path: Path, locale: Locale, arguments: Seq[Any])(implicit formatter: Formatter): String =
    apply(path, locale, Quantity.One, arguments)

  def apply(path: Path, locale: Locale, quantity: Quantity): String =
    get(path).map(_.apply(locale, quantity)).getOrElse(path.printPretty)

  def apply(path: Path, locale: Locale): String = apply(path, locale, Quantity.One)

  def only(locale: Locale): Dictionary = Dictionary(values.mapFilter(_.get(locale).toOption))

  def merge(i18n: I18n): Either[String, I18n] = values.merge(i18n.values)(_ ++ _).map(I18n.apply)

  def supports(locale: Locale): Boolean = values.forall(_.supports(locale))
}

object I18n {
  val Empty: I18n = I18n(Segments.Empty)
}
