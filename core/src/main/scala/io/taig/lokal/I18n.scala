package io.taig.lokal

import cats.syntax.all._

final case class I18n(values: Map[String, Translation]) extends AnyVal {
  @inline
  def get(key: String): Option[Translation] = values.get(key)

  def get(key: String, locale: Locale): Option[Text] = get(key).flatMap(_ apply locale)

  def only(locale: Locale): Dictionary = Dictionary(values.mapFilter(_ apply locale))

  def merge(i18n: I18n): I18n =
    I18n(
      (values.toSeq ++ i18n.values.toSeq)
        .groupBy(_._1)
        .view
        .mapValues(_.map(_._2).foldLeft(Translation.Empty)(_ ++ _))
        .toMap
    )
}
