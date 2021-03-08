package net.slozzer.babel

import simulacrum.typeclass

import scala.annotation.nowarn

@nowarn("msg=Unused import")
@typeclass
trait Encoder[A] {
  def encode(value: A): Babel

  final def contramap[B](f: B => A): Encoder[B] = value => encode(f(value))
}

object Encoder {
  implicit val string: Encoder[String] = Babel.Value(_)

  implicit def map[A](implicit encoder: Encoder[A]): Encoder[Map[String, A]] = values =>
    Babel.Object(values.view.mapValues(encoder.encode).toMap)

  implicit def option[A](implicit encoder: Encoder[A]): Encoder[Option[A]] =
    _.map(encoder.encode).getOrElse(Babel.Null)

  implicit val double: Encoder[Double] = Encoder[String].contramap(String.valueOf)

  implicit val float: Encoder[Float] = Encoder[String].contramap(String.valueOf)

  implicit val int: Encoder[Int] = Encoder[String].contramap(String.valueOf)

  implicit val long: Encoder[Long] = Encoder[String].contramap(String.valueOf)

  implicit val short: Encoder[Short] = Encoder[String].contramap(String.valueOf)
}
