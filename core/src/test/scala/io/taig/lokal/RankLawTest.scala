package io.taig.lokal

import cats.kernel.laws.discipline.OrderTests
import cats.tests.CatsSuite
import org.scalacheck.{Arbitrary, Cogen}
import org.scalatest.funsuite.AnyFunSuite

final class RankLawTest extends AnyFunSuite with CatsSuite {
  implicit val arbitrary: Arbitrary[Rank] = Arbitrary(Generators.rank)

  implicit val cogen: Cogen[Rank] = Cogens.rank

  checkAll("Rank.OrderLaws", OrderTests[Rank].order)
}
