package io.taig.lokal

trait Encoder[A, B] {
  def encode(value: A): Segments[A]
}
