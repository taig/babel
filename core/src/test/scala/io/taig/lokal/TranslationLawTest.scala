package io.taig.lokal

import cats.implicits._
import cats.laws.discipline.{FlatMapTests, SemigroupKTests}
import org.scalacheck.Arbitrary
import org.scalatest.funsuite.AnyFunSuite
import org.typelevel.discipline.scalatest.Discipline

class TranslationLawTest extends AnyFunSuite with Discipline {
  implicit def translation[A: Arbitrary]: Arbitrary[Translation[A]] =
    Arbitrary(Generators.translation(implicitly[Arbitrary[A]].arbitrary))

  checkAll(
    "Translation.FlatMapLaws",
    FlatMapTests[Translation].flatMap[Int, Int, String]
  )
  checkAll(
    "Translation.SemigroupKLaws",
    SemigroupKTests[Translation].semigroupK[Int]
  )
}
