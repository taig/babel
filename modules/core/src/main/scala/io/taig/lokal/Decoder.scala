package io.taig.lokal

trait Decoder[F[_], A] {
  def decode(values: Segments[A]): Either[Decoder.Error, F[A]]
}

object Decoder {
  final case class Error(reason: String) extends Exception(s"Failed to decode: $reason")

  def apply[F[_], A](implicit decoder: Decoder[F, A]): Decoder[F, A] = decoder
}
