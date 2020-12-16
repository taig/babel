package io.taig.babel

final case class Translation(values: Map[Locale, Text], fallback: Either[String, Text]) {
  def get(locale: Locale): Either[String, Text] =
    (values.get(locale) orElse values.get(locale.withoutCountry)).fold(fallback)(Right.apply)

  def apply(locale: Locale, quantity: Quantity, arguments: Seq[Any])(implicit formatter: Formatter): String =
    get(locale).fold(identity, _.apply(quantity, arguments))

  def apply(locale: Locale, arguments: Seq[Any])(implicit formatter: Formatter): String =
    apply(locale, Quantity.One, arguments)

  def apply(locale: Locale, quantity: Quantity): String = get(locale).fold(identity, _.apply(quantity))

  def apply(locale: Locale): String = apply(locale, Quantity.One)

  def ++(translation: Translation): Translation =
    Translation(
      values ++ translation.values,
      translation.fallback match {
        case right @ Right(_) => right
        case left @ Left(_) =>
          fallback match {
            case right @ Right(_) => right
            case Left(_)          => left
          }
      }
    )

  def add(locale: Locale, text: Text): Translation = Translation(values + (locale -> text), fallback)

  def supports(locale: Locale): Boolean = get(locale).isRight
}

object Translation {
  def of(fallback: => String)(translations: (Locale, Text)*): Translation =
    Translation(translations.toMap, Left(fallback))

  def fallback(value: String): Translation = Translation(Map.empty, fallback = Left(value))

  def universal(text: Text): Translation = Translation(Map.empty, fallback = Right(text))
}
