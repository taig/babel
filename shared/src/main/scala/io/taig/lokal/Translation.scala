package io.taig.lokal

import java.util.Locale

import cats.Eq
import cats.implicits._
import io.taig.lokal.implicits._

case class Translation[A](locale: Locale, value: A)

object Translation {
  implicit def eq[A: Eq]: Eq[Translation[A]] = Eq.instance { (x, y) =>
    x.locale === y.locale && x.value === y.value
  }
}
