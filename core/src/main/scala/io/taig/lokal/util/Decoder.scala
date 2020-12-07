package io.taig.lokal.util

import io.taig.lokal.Mode
import simulacrum.typeclass

@typeclass
trait Decoder[A] {
  def apply(mode: Mode, strict: Boolean, input: String): Either[Decoder.Error, A]
}

object Decoder {
  final case class Error(message: String, cause: Option[Throwable]) extends RuntimeException(message, cause.orNull)
}
