package net.slozzer.babel.sample.backend

import net.slozzer.babel.generic.auto._
import net.slozzer.babel.{Decoder, StringFormat1}

final case class I18n(app: I18n.App, index: I18n.Index)

object I18n {
  final case class App(name: String)

  final case class Index(title: String, headline: String, message: StringFormat1, label: String)

  implicit def decoder: Decoder[I18n] = deriveDecoder[I18n]
}
