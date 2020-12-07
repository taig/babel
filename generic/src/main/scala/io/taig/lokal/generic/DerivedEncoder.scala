package io.taig.lokal.generic

import io.taig.lokal.util.Encoder
import shapeless._

trait DerivedEncoder[A] extends Encoder[A]

object DerivedEncoder {
  implicit val hnil: DerivedEncoder[HNil] = new DerivedEncoder[HNil] {
    override def apply(value: HNil): String = ""
  }

  implicit def hcons[A, B <: HList]: DerivedEncoder[A :: B] = new DerivedEncoder[A :: B] {
    override def apply(value: A :: B): String = ???
  }
}
