package io.taig.lokal

import cats.implicits._
import cats.kernel.laws.discipline.OrderTests
import org.scalacheck.{Arbitrary, Cogen}
import org.scalatest.funsuite.AnyFunSuite
import org.typelevel.discipline.scalatest.Discipline

final class RankLawTest extends AnyFunSuite with Discipline {
  implicit val arbitrary: Arbitrary[Rank] = Arbitrary(Generators.rank)

  implicit val cogen: Cogen[Rank] = Cogens.rank

  checkAll("Rank.OrderLaws", OrderTests[Rank].order)
}
