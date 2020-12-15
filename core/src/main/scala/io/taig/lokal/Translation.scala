package io.taig.lokal

final case class Translation(values: Map[Locale, Text], fallback: Option[Text]) {
  def get(locale: Locale): Option[Text] = values.get(locale)

  def apply(locale: Locale): Option[Text] = get(locale) orElse get(locale.withoutCountry) orElse fallback

  def ++(translation: Translation): Translation =
    Translation(values ++ translation.values, translation.fallback orElse fallback)
}

object Translation {
  val Empty: Translation = Translation(Map.empty, None)

  def one(locale: Locale, text: Text): Translation = Translation(Map(locale -> text), fallback = None)
}
