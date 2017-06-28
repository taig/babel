package io.taig.lokal

import cats.implicits._
import cats.{ Eq, Show }

case class Country( value: String ) extends AnyVal

object Country extends Countries {
    implicit val eq: Eq[Country] = Eq.by( _.value )

    implicit val show: Show[Country] = Show[String].contramap( _.value )
}