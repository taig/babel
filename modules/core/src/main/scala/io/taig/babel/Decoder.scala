package io.taig.babel

trait Decoder[F[_], A] {
  def decode(path: Path, values: Segments[A]): Either[Decoder.Error, F[A]]

  final def decode(values: Segments[A]): Either[Decoder.Error, F[A]] = decode(Path.Empty, values)
}

object Decoder {
  final case class Error(reason: String) extends Exception(s"Failed to decode: $reason")

  def apply[F[_], A](implicit decoder: Decoder[F, A]): Decoder[F, A] = decoder
}
