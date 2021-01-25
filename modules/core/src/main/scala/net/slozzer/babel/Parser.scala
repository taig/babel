package net.slozzer.babel

abstract class Parser {
  def parse(value: String): Either[Parser.Error, Babel]

  final def parseAll(translations: Translations[String]): Either[Parser.Error, Translations[Babel]] =
    translations.values.foldLeft[Either[Parser.Error, Translations[Babel]]](Right(Translations.Empty)) {
      case (Right(babel), (locale, value)) =>
        parse(value) match {
          case Right(result)  => Right(babel ++ Translations.one(locale, result))
          case left @ Left(_) => left.asInstanceOf[Either[Parser.Error, Translations[Babel]]]
        }
      case (left @ Left(_), _) => left
    }
}

object Parser {
  final case class Error(message: String, cause: Option[Throwable])
      extends Exception(s"Parser failed: $message", cause.orNull)

  object Error {
    def typeMismatch(tpe: String, cause: Option[Throwable]): Error = Error(s"Type mismatch [$tpe]", cause)
  }
}
