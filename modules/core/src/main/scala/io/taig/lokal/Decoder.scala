package io.taig.lokal

trait Decoder[F[_], A] {
  def decode(values: Segments[A]): Either[String, F[A]]
}

object Decoder {
  def apply[F[_], A](implicit decoder: Decoder[F, A]): Decoder[F, A] = decoder
}
