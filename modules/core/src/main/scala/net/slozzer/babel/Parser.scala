package net.slozzer.babel

import java.nio.charset.{Charset, StandardCharsets}

import simulacrum.typeclass

@typeclass
trait Parser[A] {
  def parse(bytes: Array[Byte]): Either[Parser.Error, A]

  final def map[B](f: A => B): Parser[B] = value => parse(value).map(f)

  final def emap[B](f: A => Either[Parser.Error, B]): Parser[B] = value => parse(value).flatMap(f)
}

object Parser {
  final case class Error(message: String, cause: Option[Throwable])
      extends Exception(s"Parser failed: $message", cause.orNull)

  object Error {
    def typeMismatch(tpe: String, cause: Option[Throwable]): Error = Error(s"Type mismatch [$tpe]", cause)
  }

  def numeric[A](tpe: String, f: String => A): Parser[A] = text.emap { value =>
    try Right(f(value))
    catch {
      case exception: NumberFormatException => Left(Error.typeMismatch(tpe, Some(exception)))
    }
  }

  def string(charset: Charset): Parser[String] = bytes => Right(new String(bytes, charset))

  implicit val bytes: Parser[Array[Byte]] = Right.apply[Parser.Error, Array[Byte]]

  implicit val text: Parser[String] = string(StandardCharsets.UTF_8)

  implicit val int: Parser[Int] = numeric("Int", _.toInt)
}
