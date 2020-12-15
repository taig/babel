package io.taig.lokal

import simulacrum.typeclass

@typeclass
trait Parser[A] {
  def parse(value: String): Either[String, A]

  final def map[B](f: A => B): Parser[B] = value => parse(value).map(f)

  final def emap[B](f: A => Either[String, B]): Parser[B] = value => parse(value).flatMap(f)
}

object Parser {
  implicit val string: Parser[String] = Right.apply[String, String]

  implicit val int: Parser[Int] = _.toIntOption.toRight("Int")
}
