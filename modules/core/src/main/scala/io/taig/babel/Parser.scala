package io.taig.babel

abstract class Parser {
  def parse(value: String): Either[Parser.Error, Babel]

  final def parseAll(translations: Translations[String]): Either[Parser.Error, Translations[Babel]] =
    translations.toList.foldLeft[Either[Parser.Error, Translations[Babel]]](Right(Translations.Empty)) {
      case (Right(babel), translation) =>
        parse(translation.value) match {
          case Right(value)   => Right(babel concat Translations.of(translation.as(value)))
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
