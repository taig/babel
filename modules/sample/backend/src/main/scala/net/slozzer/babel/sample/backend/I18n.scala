package net.slozzer.babel.sample.backend

import net.slozzer.babel.generic.semiauto._
import net.slozzer.babel.{Decoder, StringFormat1}

final case class I18n(app: I18n.App, index: I18n.Index)

object I18n {
  final case class App(name: String)

  final case class Index(title: String, headline: String, message: StringFormat1, label: String)

  implicit val decoder: Decoder[I18n] = {
    implicit val app = deriveDecoder[App]
    implicit val index = deriveDecoder[Index]
    deriveDecoder[I18n]
  }
}
