package io.taig.lokal

import cats.Show
import cats.implicits._

case class Language( value: String ) extends AnyVal

object Language extends Languages {
    implicit val show: Show[Language] = Show[String].contramap( _.value )
}