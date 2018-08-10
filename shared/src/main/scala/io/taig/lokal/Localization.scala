package io.taig.lokal

import cats.{Eq, Show}
import cats.implicits._
import cats.data.NonEmptyList

case class Localization[A](identifier: LocalizationIdentifier, value: A) {
  def &(localization: Localization[A]): Translation[A] =
    Translation(NonEmptyList.of(this, localization))

  override def toString: String = s"""$identifier"$value""""
}

object Localization {
  implicit def eq[A: Eq]: Eq[Localization[A]] = Eq.instance { (a, b) ⇒
    a.identifier === b.identifier && a.value === b.value
  }

  implicit def show[A]: Show[Localization[A]] = Show.fromToString
}
