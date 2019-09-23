package io.taig.lokal

import cats._
import cats.implicits._

final case class Country(value: String) extends AnyVal

object Country {
  implicit val eq: Eq[Country] = Eq.by(_.value)

  implicit val show: Show[Country] = _.value
}
