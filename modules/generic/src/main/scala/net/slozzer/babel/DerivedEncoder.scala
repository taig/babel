package net.slozzer.babel

import shapeless._
import simulacrum.typeclass

@typeclass
trait DerivedEncoder[A] extends Encoder[A] {
  override def encode(value: A): Babel
}

object DerivedEncoder {
  implicit val hnil: DerivedEncoder[HNil] = _ => Babel.Empty

//  implicit def hcons[K <: Symbol, A, T <: HList](implicit
//      key: Witness.Aux[K],
//      tail: DerivedEncoder[T, A]
//  ): DerivedEncoder[FieldType[K, A] :: T, A] = new DerivedEncoder[FieldType[K, A] :: T, A] {
//    override def encode(value: FieldType[K, A] :: T): Segments[A] =
//      Segments.one(key.value.name, value.head) ++ tail.encode(value.tail)
//  }

  implicit def encoderLabelledGeneric[A, B <: HList, C](implicit
      generic: LabelledGeneric.Aux[A, B],
      encoder: DerivedEncoder[B]
  ): DerivedEncoder[A] = value => encoder.encode(generic.to(value))
}
