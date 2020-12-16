package io.taig.babel

import simulacrum.typeclass

@typeclass
trait Parser[A] {
  def parse(value: String): Either[Parser.Error, A]

  final def map[B](f: A => B): Parser[B] = value => parse(value).map(f)

  final def emap[B](f: A => Either[Parser.Error, B]): Parser[B] = value => parse(value).flatMap(f)
}

object Parser {
  final case class Error(tpe: String) extends Exception(s"Failed to parse: $tpe")

  implicit val string: Parser[String] = Right.apply[Parser.Error, String]

  implicit val int: Parser[Int] = _.toIntOption.toRight(Error("Int"))
}
