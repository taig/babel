package io.taig.lokal

import cats.implicits._
import cats.{ Eq, Show }

case class Language( value: String ) extends AnyVal

object Language extends Languages {
    implicit val eq: Eq[Language] = Eq.by( _.value )

    implicit val show: Show[Language] = Show[String].contramap( _.value )
}