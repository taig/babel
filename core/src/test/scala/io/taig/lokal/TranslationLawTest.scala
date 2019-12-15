package io.taig.lokal

import cats.Eq
import cats.implicits._
import cats.laws.discipline.{MonadTests, MonoidKTests}
import org.scalacheck.Arbitrary
import org.scalatest.funsuite.AnyFunSuite
import org.typelevel.discipline.scalatest.Discipline

final class TranslationLawTest extends AnyFunSuite with Discipline {
  implicit def arbitrary[A: Arbitrary]: Arbitrary[Translation[A]] =
    Arbitrary(Generators.translations(Arbitrary.arbitrary[A]))

  implicit def eq[A: Eq]: Eq[Translation[A]] =
    (x, y) =>
      Generators.locales.forall(locale =>
        x.translate(locale) eqv y.translate(locale)
      )

  checkAll(
    "Translation.MonadLaws",
    MonadTests[Translation].monad[Int, Int, String]
  )
  checkAll(
    "Translation.MonoidKLaws",
    MonoidKTests[Translation].monoidK[Int]
  )
}
