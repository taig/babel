package io.taig.lokal

final case class I18n(values: Map[Path, Translation]) extends AnyVal {
  @inline
  def get(path: Path): Option[Translation] = values.get(path)

  def get(path: Path, locale: Locale): Option[Text] = get(path).flatMap(_ apply locale)

  def only(locale: Locale): Dictionary =
    Dictionary(values.view.mapValues(_ apply locale).collect { case (key, Some(value)) => (key, value) }.toMap)

  def merge(i18n: I18n): I18n = I18n(
    (values.toSeq ++ i18n.values.toSeq)
      .groupBy(_._1)
      .view
      .mapValues(_.map(_._2).foldLeft(Translation.Empty)(_ ++ _))
      .toMap
  )
}
