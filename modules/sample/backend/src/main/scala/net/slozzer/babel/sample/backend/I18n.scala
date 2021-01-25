package net.slozzer.babel.sample.backend

import net.slozzer.babel.Decoder
import net.slozzer.babel.generic

//final case class I18n(app: I18n.App, index: I18n.Index)
final case class I18n(yolo: String)

object I18n {
  final case class App(name: String)

  final case class Index(title: String, headline: String, message: String, label: String)

  implicit val decoder: Decoder[I18n] = generic.auto.derivedDecoder[I18n]
}
