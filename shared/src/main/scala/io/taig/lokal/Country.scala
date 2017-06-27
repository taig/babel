package io.taig.lokal

import cats.implicits._
import cats.Show

case class Country( value: String ) extends AnyVal

object Country extends Countries {
    implicit val show: Show[Country] = Show[String].contramap( _.value )
}