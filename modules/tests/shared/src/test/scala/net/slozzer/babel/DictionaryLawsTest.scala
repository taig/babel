package net.slozzer.babel

import _root_.cats.kernel.laws.discipline.EqTests
import _root_.cats.laws.discipline.TraverseTests
import munit.DisciplineSuite
import net.slozzer.babel.cats._
import org.scalacheck.Arbitrary

final class DictionaryLawsTest extends DisciplineSuite {
  implicit def arbitrary[A: Arbitrary]: Arbitrary[Dictionary[A]] =
    Arbitrary(Generators.dictionary(Arbitrary.arbitrary[A]))

  import Cogenerators.dictionary

  checkAll("Dictionary", TraverseTests[Dictionary].traverse[Int, Double, String, Long, Option, Option])
  checkAll("Dictionary", EqTests[Dictionary[Int]].eqv)
}
