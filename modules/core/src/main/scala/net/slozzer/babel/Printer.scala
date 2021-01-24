package net.slozzer.babel

import java.nio.charset.{Charset, StandardCharsets}

import simulacrum.typeclass

@typeclass
trait Printer[A] {
  def print(value: A): Array[Byte]

  final def contramap[B](f: B => A): Printer[B] = value => print(f(value))
}

object Printer {
  def string(charset: Charset): Printer[String] = _.getBytes(charset)

  implicit val text: Printer[String] = string(StandardCharsets.UTF_8)

  implicit val int: Printer[Int] = Printer[String].contramap(_.toString)
}
