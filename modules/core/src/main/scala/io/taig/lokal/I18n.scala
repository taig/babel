package io.taig.lokal

import scala.collection.immutable.Map

final case class I18n(values: Map[Path, Translation]) extends AnyVal {
  @inline
  def get(path: Path): Option[Translation] = values.get(path)

  def get(path: Path, locale: Locale): Option[Text] = get(path).flatMap(_ get locale)

  def apply(path: Path, locale: Locale, quantity: Quantity, arguments: Seq[Any])(
      implicit formatter: Formatter
  ): String =
    get(path, locale).map(_.apply(quantity, arguments)).getOrElse(path.printPretty)

  def apply(path: Path, locale: Locale, arguments: Seq[Any])(implicit formatter: Formatter): String =
    apply(path, locale, Quantity.One, arguments)

  def apply(path: Path, locale: Locale, quantity: Quantity): String =
    get(path, locale).map(_.apply(quantity)).getOrElse(path.printPretty)

  def apply(path: Path, locale: Locale): String = apply(path, locale, Quantity.One)

  def only(locale: Locale): Dictionary =
    Dictionary(values.view.mapValues(_ get locale).collect { case (key, Some(value)) => (key, value) }.toMap)

  def merge(i18n: I18n): I18n = I18n(
    (values.toSeq ++ i18n.values.toSeq)
      .groupBy(_._1)
      .view
      .mapValues(_.map(_._2).foldLeft(Translation.Empty)(_ ++ _))
      .toMap
  )
}

object I18n {
  val Empty: I18n = I18n(Map.empty)

  def forLanguage(language: Language, values: Map[Option[Country], Dictionary]): I18n = values.foldLeft(Empty) {
    case (i18n, (country, dictionary)) => i18n merge dictionary.toI18n(Locale(language, country))
  }
}
