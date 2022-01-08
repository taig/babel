package io.taig.babel

import _root_.cats.kernel.laws.discipline.EqTests
import _root_.cats.laws.discipline.{SemigroupKTests, TraverseTests}
import io.taig.babel.cats._
import munit.DisciplineSuite
import org.scalacheck.Arbitrary

final class NonEmptyTranslationsLawsTest extends DisciplineSuite {
  implicit def arbitrary[A: Arbitrary]: Arbitrary[NonEmptyTranslations[A]] =
    Arbitrary(Generators.nonEmptyTranslations(Arbitrary.arbitrary[A]))

  import Cogenerators.nonEmptyTranslations

  checkAll(
    "NonEmptyTranslations",
    TraverseTests[NonEmptyTranslations].traverse[Int, Double, String, Long, Option, Option]
  )
  checkAll("NonEmptyTranslations", SemigroupKTests[NonEmptyTranslations].semigroupK[Int])
  checkAll("NonEmptyTranslations", EqTests[NonEmptyTranslations[Int]].eqv)
}
