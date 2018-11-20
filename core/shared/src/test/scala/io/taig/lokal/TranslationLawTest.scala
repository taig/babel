package io.taig.lokal

import cats.laws.discipline.FlatMapTests
import cats.tests.CatsSuite
import org.scalacheck.Arbitrary

class TranslationLawTest extends CatsSuite {
  implicit def translation[A: Arbitrary]: Arbitrary[Translation[A]] =
    Arbitrary(Generators.translation(implicitly[Arbitrary[A]].arbitrary))

  checkAll("Translation.FlatMapLaws", FlatMapTests[Translation].flatMap[Int, Int, String])
}
