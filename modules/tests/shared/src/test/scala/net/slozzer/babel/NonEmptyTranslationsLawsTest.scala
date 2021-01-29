package net.slozzer.babel

import _root_.cats.kernel.laws.discipline.EqTests
import _root_.cats.laws.discipline.TraverseTests
import munit.DisciplineSuite
import net.slozzer.babel.cats._
import org.scalacheck.Arbitrary

final class NonEmptyTranslationsLawsTest extends DisciplineSuite {
  implicit def arbitrary[A: Arbitrary]: Arbitrary[NonEmptyTranslations[A]] =
    Arbitrary(Generators.dictionary(Arbitrary.arbitrary[A]))

  import Cogenerators.dictionary

  checkAll("Dictionary", TraverseTests[NonEmptyTranslations].traverse[Int, Double, String, Long, Option, Option])
  checkAll("Dictionary", EqTests[NonEmptyTranslations[Int]].eqv)
}
