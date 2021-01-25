package net.slozzer.sample

import net.slozzer.babel.generic.auto._
import net.slozzer.babel.{Decoder, Encoder, Quantities, StringFormat1}

final case class I18n(app: I18n.App, index: I18n.Index)

object I18n {
  final case class App(name: String)

  final case class Index(title: String, headline: String, message: Quantities[StringFormat1], label: String)

  implicit val decoder: Decoder[I18n] = deriveDecoder[I18n]

  implicit val encoder: Encoder[I18n] = deriveEncoder[I18n]
}