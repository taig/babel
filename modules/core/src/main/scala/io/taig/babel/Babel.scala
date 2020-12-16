package io.taig.babel

final case class Babel(values: Segments[Translation]) extends AnyVal {
  @inline
  def get(path: Path): Option[Translation] = values.getLeaf(path)

  def apply(path: Path, locale: Locale, quantity: Quantity, arguments: Seq[Any])(
      implicit formatter: Formatter
  ): String =
    get(path).map(_.apply(locale, quantity, arguments)).getOrElse(path.printPlaceholder)

  def apply(path: Path, locale: Locale, arguments: Seq[Any])(implicit formatter: Formatter): String =
    apply(path, locale, Quantity.One, arguments)

  def apply(path: Path, locale: Locale, quantity: Quantity): String =
    get(path).map(_.apply(locale, quantity)).getOrElse(path.printPlaceholder)

  def apply(path: Path, locale: Locale): String = apply(path, locale, Quantity.One)

  def only(locale: Locale): Dictionary = Dictionary(values.mapFilter(_.get(locale).toOption))

  def merge(i18n: Babel): Either[String, Babel] = values.merge(i18n.values)(_ ++ _).map(Babel.apply)

  def missingTranslations(locale: Locale): Set[Path] =
    values
      .mapFilter(translation => if (!translation.supports(locale)) Some(translation) else None)
      .toMap
      .keySet
}

object Babel {
  val Empty: Babel = Babel(Segments.Empty)
}
