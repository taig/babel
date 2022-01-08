package io.taig.babel

import _root_.cats.kernel.laws.discipline.EqTests
import _root_.cats.laws.discipline.TraverseTests
import io.taig.babel.cats._
import munit.DisciplineSuite
import org.scalacheck.Arbitrary

final class TranslationLawsTest extends DisciplineSuite {
  implicit def arbitrary[A: Arbitrary]: Arbitrary[Translation[A]] =
    Arbitrary(Generators.translation(Arbitrary.arbitrary[A]))

  import Cogenerators.translation

  checkAll("Translation", TraverseTests[Translation].traverse[Int, Double, String, Long, Option, Option])
  checkAll("Translation", EqTests[Translation[Int]].eqv)
}
