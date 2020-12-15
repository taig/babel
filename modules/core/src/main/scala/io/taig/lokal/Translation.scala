package io.taig.lokal

final case class Translation(values: Map[Locale, Text], fallback: Option[Text]) {
  def get(locale: Locale): Option[Text] = values.get(locale) orElse values.get(locale.withoutCountry) orElse fallback

  def apply(locale: Locale, quantity: Quantity, arguments: Seq[Any])(implicit formatter: Formatter): Option[String] =
    get(locale).map(_.apply(quantity, arguments))

  def apply(locale: Locale, arguments: Seq[Any])(implicit formatter: Formatter): Option[String] =
    apply(locale, Quantity.One, arguments)

  def apply(locale: Locale, quantity: Quantity): Option[String] = get(locale).map(_.apply(quantity))

  def apply(locale: Locale): Option[String] = apply(locale, Quantity.One)

  def ++(translation: Translation): Translation =
    Translation(values ++ translation.values, translation.fallback orElse fallback)

  def supports(locale: Locale): Boolean = get(locale).isDefined
}

object Translation {
  val Empty: Translation = Translation(Map.empty, None)

  def one(locale: Locale, text: Text): Translation = Translation(Map(locale -> text), fallback = None)

  def fallback(text: Text): Translation = Translation(Map.empty, fallback = Some(text))
}
