package net.slozzer.babel

import _root_.cats.kernel.laws.discipline.EqTests
import munit.DisciplineSuite
import net.slozzer.babel.cats._
import org.scalacheck.Arbitrary

final class QuantityLawsTest extends DisciplineSuite {
  implicit val arbitrary: Arbitrary[Quantity] = Arbitrary(Generators.quantity)

  import Cogenerators.quantity

  checkAll("Quantity", EqTests[Quantity].eqv)
}
