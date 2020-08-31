package io.taig.lokal

import cats.Eq
import cats.laws.discipline.{MonadTests, SemigroupKTests}
import cats.tests.CatsSuite
import org.scalacheck.Arbitrary
import org.scalatest.funsuite.AnyFunSuite

final class DictionaryLawTest extends AnyFunSuite with CatsSuite {
  implicit def arbitrary[A: Arbitrary]: Arbitrary[Dictionary[A]] =
    Arbitrary(Generators.dictionaries(Arbitrary.arbitrary[A]))

  implicit def eq[A: Eq]: Eq[Dictionary[A]] =
    (x, y) => Generators.locales.forall(locale => Eq[A].eqv(x.translate(locale), y.translate(locale)))

  checkAll("Dictionary.MonadLaws", MonadTests[Dictionary].monad[Int, Int, String])
  checkAll("Dictionary.SemigroupKLaws", SemigroupKTests[Dictionary].semigroupK[Int])
}
