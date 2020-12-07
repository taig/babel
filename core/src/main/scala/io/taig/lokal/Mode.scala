package io.taig.lokal

sealed abstract class Mode extends Product with Serializable

object Mode {
  final case class Monoglot(locale: Locale) extends Mode
  final case object Polyglot extends Mode
}
