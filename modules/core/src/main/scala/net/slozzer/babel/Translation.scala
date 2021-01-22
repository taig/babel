package net.slozzer.babel

final case class Translation[A](fallback: Either[A, A], translations: Map[Locale, A]) {
  def get(locale: Locale): Either[A, A] =
    (translations.get(locale) orElse translations.get(locale.withoutCountry)).fold(fallback)(Right.apply)

  def apply(locale: Locale): A = get(locale).merge

  def locales: Set[Locale] = translations.keySet

  def ++(translation: Translation[A]): Translation[A] = Translation(
    translation.fallback match {
      case right @ Right(_) => right
      case left @ Left(_) =>
        fallback match {
          case right @ Right(_) => right
          case Left(_)          => left
        }
    },
    translations ++ translation.translations
  )

  def add(locale: Locale, value: A): Translation[A] = Translation(fallback, translations + (locale -> value))

  def supports(locale: Locale): Boolean = get(locale).isRight

  def map[B](f: A => B): Translation[B] =
    Translation(
      fallback match {
        case Left(value)  => Left(f(value))
        case Right(value) => Right(f(value))
      },
      translations.view.mapValues(f).toMap
    )

  override def toString: String =
    s"""{${translations.map { case (locale, text) => s"$locale: $text" }.mkString(", ")}, """ +
      s"""*: ${fallback.map(_.toString).merge}}"""
}

object Translation {
  def default[A](value: => A, translations: (Locale, A)*): Translation[A] =
    Translation(fallback = Right(value), translations.toMap)

  def fallback[A](value: => A, translations: (Locale, A)*): Translation[A] =
    Translation(fallback = Left(value), translations.toMap)
}
