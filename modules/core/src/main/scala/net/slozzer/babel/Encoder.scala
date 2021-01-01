package net.slozzer.babel

trait Encoder[F[_], A] {
  def encode(value: F[A]): Segments[A]
}

object Encoder {
  def apply[F[_], A](implicit encoder: Encoder[F, A]): Encoder[F, A] = encoder
}
