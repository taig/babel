package io.taig.lokal

trait Decoder[A, B] {
  def decode(values: Segments[A]): Either[String, B]
}

object Decoder {
  def apply[A, B](implicit decoder: Decoder[A, B]): Decoder[A, B] = decoder
}
