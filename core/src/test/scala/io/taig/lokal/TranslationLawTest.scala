package io.taig.lokal

import cats.kernel.laws.discipline.SemigroupTests
import cats.laws.discipline.{FlatMapTests, SemigroupKTests}
import cats.tests.CatsSuite
import org.scalacheck.Arbitrary

class TranslationLawTest extends CatsSuite {
  implicit def translation[A: Arbitrary]: Arbitrary[Translation[A]] =
    Arbitrary(Generators.translation(implicitly[Arbitrary[A]].arbitrary))

  checkAll(
    "Translation.FlatMapLaws",
    FlatMapTests[Translation].flatMap[Int, Int, String]
  )
  checkAll(
    "Translation.SemigroupLaws",
    SemigroupTests[Translation[Int]].semigroup
  )
  checkAll(
    "Translation.SemigroupKLaws",
    SemigroupKTests[Translation].semigroupK[Int]
  )
}
