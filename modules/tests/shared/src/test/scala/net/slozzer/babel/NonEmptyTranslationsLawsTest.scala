package net.slozzer.babel

import _root_.cats.kernel.laws.discipline.EqTests
import _root_.cats.laws.discipline.TraverseTests
import munit.DisciplineSuite
import net.slozzer.babel.cats._
import org.scalacheck.Arbitrary

final class NonEmptyTranslationsLawsTest extends DisciplineSuite {
  implicit def arbitrary[A: Arbitrary]: Arbitrary[NonEmptyTranslations[A]] =
    Arbitrary(Generators.nonEmptyTranslations(Arbitrary.arbitrary[A]))

  import Cogenerators.nonEmptyTranslations

  checkAll(
    "NonEmptyTranslations",
    TraverseTests[NonEmptyTranslations].traverse[Int, Double, String, Long, Option, Option]
  )
  checkAll("NonEmptyTranslations", EqTests[NonEmptyTranslations[Int]].eqv)
}
