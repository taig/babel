package io.taig.lokal

import cats.kernel.laws.discipline.EqTests
import org.scalacheck.{Arbitrary, Cogen}
import org.scalatest.funsuite.AnyFunSuite
import org.typelevel.discipline.scalatest.Discipline

final class LocaleLawTest extends AnyFunSuite with Discipline {
  implicit val arbitrary: Arbitrary[Locale] = Arbitrary(Generators.locale)

  implicit val cogen: Cogen[Locale] = Cogens.locale

  checkAll("Locale.EqLaws", EqTests[Locale].eqv)
}
