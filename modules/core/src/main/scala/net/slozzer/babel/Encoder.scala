package net.slozzer.babel

import simulacrum.typeclass

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
}
