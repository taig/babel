package io.taig.babel

import _root_.cats.kernel.laws.discipline.EqTests
import io.taig.babel.cats._
import munit.DisciplineSuite
import org.scalacheck.Arbitrary

final class QuantityLawsTest extends DisciplineSuite {
  implicit val arbitrary: Arbitrary[Quantity] = Arbitrary(Generators.quantity)

  import Cogenerators.quantity

  checkAll("Quantity", EqTests[Quantity].eqv)
}
