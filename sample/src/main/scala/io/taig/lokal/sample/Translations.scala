package io.taig.lokal.sample

import io.circe.generic.JsonCodec
import io.taig.lokal.{Dictionary, Translation}
import io.taig.lokal.circe._

@JsonCodec(encodeOnly = true)
final case class Translations(app: Translations.App)

object Translations {
  @JsonCodec(encodeOnly = true)
  final case class App(name: Dictionary)
}
