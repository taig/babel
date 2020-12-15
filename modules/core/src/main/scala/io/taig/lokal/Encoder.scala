package io.taig.lokal

trait Encoder[F[_], A] {
  def encode(value: F[A]): Segments[A]
}
