package io.taig.lokal

import cats.Eq
import cats.laws.discipline.{MonadTests, MonoidKTests}
import cats.tests.CatsSuite
import org.scalacheck.Arbitrary
import org.scalatest.funsuite.AnyFunSuite

final class TranslationLawTest extends AnyFunSuite with CatsSuite {
  implicit def arbitrary[A: Arbitrary]: Arbitrary[I18n[A]] =
    Arbitrary(Generators.translations(Arbitrary.arbitrary[A]))

  implicit def eq[A: Eq]: Eq[I18n[A]] =
    (x, y) => Generators.locales.forall(locale => Eq[Option[A]].eqv(x.translate(locale), y.translate(locale)))

  checkAll("Translation.MonadLaws", MonadTests[I18n].monad[Int, Int, String])
  checkAll("Translation.MonoidKLaws", MonoidKTests[I18n].monoidK[Int])
}
