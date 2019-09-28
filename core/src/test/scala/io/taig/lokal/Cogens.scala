package io.taig.lokal

import org.scalacheck.Cogen
import org.scalacheck.derive.MkCogen
import org.scalacheck.ScalacheckShapeless._

object Cogens {
  val locale: Cogen[Locale] = MkCogen[Locale].cogen

  val rank: Cogen[Rank] = MkCogen[Rank].cogen
}
