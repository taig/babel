package io.taig.sample

import io.taig.babel.generic.auto._
import io.taig.babel.{Decoder, Encoder, Quantities, StringFormat1}

final case class I18n(app: I18n.App, index: I18n.Index)

object I18n {
  final case class App(name: String)

  final case class Index(
      page: Page,
      headline: String,
      introduction: String,
      message: Quantities[StringFormat1],
      label: String
  )

  final case class Page(title: String, description: Option[String])

  implicit val decoder: Decoder[I18n] = deriveDecoder[I18n]

  implicit val encoder: Encoder[I18n] = deriveEncoder[I18n]
}
