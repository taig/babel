package io.taig.lokal

import cats.kernel.laws.discipline.EqTests
import cats.tests.CatsSuite
import org.scalacheck.{Arbitrary, Cogen}
import org.scalatest.funsuite.AnyFunSuite

final class LocaleLawTest extends AnyFunSuite with CatsSuite {
  implicit val arbitrary: Arbitrary[Locale] = Arbitrary(Generators.locale)

  implicit val cogen: Cogen[Locale] = Cogens.locale

  checkAll("Locale.EqLaws", EqTests[Locale].eqv)
}
