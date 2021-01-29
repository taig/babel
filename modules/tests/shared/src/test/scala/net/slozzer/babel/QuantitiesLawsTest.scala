package net.slozzer.babel

import _root_.cats.kernel.laws.discipline.EqTests
import _root_.cats.laws.discipline.TraverseTests
import _root_.cats.laws.discipline.SemigroupKTests
import munit.DisciplineSuite
import net.slozzer.babel.cats._
import org.scalacheck.Arbitrary

final class QuantitiesLawsTest extends DisciplineSuite {
  implicit def arbitrary[A: Arbitrary]: Arbitrary[Quantities[A]] =
    Arbitrary(Generators.quantities(Arbitrary.arbitrary[A]))

  import Cogenerators.quantities

  checkAll("Quantities", TraverseTests[Quantities].traverse[Int, Double, String, Long, Option, Option])
  checkAll("Quantities", SemigroupKTests[Quantities].semigroupK[Int])
  checkAll("Quantities", EqTests[Quantities[Int]].eqv)
}
