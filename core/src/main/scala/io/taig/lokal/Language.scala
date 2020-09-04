package io.taig.lokal

import cats.{Eq, Show}

final case class Language(value: String) extends AnyVal

object Language {
  implicit val eq: Eq[Language] = Eq.by(_.value)

  implicit val show: Show[Language] = _.value
}
