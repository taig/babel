package io.taig.babel

final case class Babel(values: Segments[Translation]) extends AnyVal {
  @inline
  def get(path: Path): Option[Translation] = values.getLeaf(path)

  def apply(path: Path, locale: Locale, quantity: Int, arguments: Seq[Any])(implicit
      formatter: Formatter
  ): String =
    get(path).map(_.apply(locale, quantity, arguments)).getOrElse(path.printPlaceholder)

  def apply(path: Path, locale: Locale, arguments: Seq[Any])(implicit formatter: Formatter): String =
    apply(path, locale, quantity = 1, arguments)

  def apply(path: Path, locale: Locale, quantity: Int): String =
    get(path).map(_.apply(locale, quantity)).getOrElse(path.printPlaceholder)

  def apply(path: Path, locale: Locale): String = apply(path, locale, quantity = 1)

  def locales: Set[Locale] = values.values[Translation].flatMap(_.locales)

  def only(locale: Locale): Dictionary = Dictionary(values.mapFilter(_.get(locale).toOption))

  def merge(babel: Babel): Either[String, Babel] = values.merge(babel.values)(_ ++ _).map(Babel.apply)

  def missingTranslations(locale: Locale): Set[Path] =
    values
      .mapFilter(translation => if (!translation.supports(locale)) Some(translation) else None)
      .toMap
      .keySet
}

object Babel {
  val Empty: Babel = Babel(Segments.Empty)
}
