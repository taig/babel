package io.taig.lokal

import shapeless._
import shapeless.labelled._

trait DerivedEncoder[A, B] {
  def encode(value: A): Segments[B]
}

object DerivedEncoder extends DerivedEncoder1 {
  def apply[A, B](implicit encoder: DerivedEncoder[A, B]): DerivedEncoder[A, B] = encoder

  implicit def segments[A, K <: Symbol, V](
      implicit
      key: Witness.Aux[K],
      encoder: DerivedEncoder[V, A]
  ): DerivedEncoder[FieldType[K, V], A] =
    new DerivedEncoder[FieldType[K, V], A] {
      override def encode(value: FieldType[K, V]): Segments[A] =
        Segments(Map(key.value.name -> Right(encoder.encode(value))))
    }

  implicit def hnil[A]: DerivedEncoder[HNil, A] = new DerivedEncoder[HNil, A] {
    override def encode(value: HNil): Segments[A] = Segments.Empty
  }

  implicit def hcons[A, K <: Symbol, V, T <: HList](
      implicit
      head: DerivedEncoder[FieldType[K, V], A],
      tail: DerivedEncoder[T, A]
  ): DerivedEncoder[FieldType[K, V] :: T, A] = new DerivedEncoder[FieldType[K, V] :: T, A] {
    override def encode(value: FieldType[K, V] :: T): Segments[A] =
      head.encode(value.head) ++ tail.encode(value.tail)
  }

  implicit def decoderLabelledGeneric[A, B <: HList, C](
      implicit
      generic: LabelledGeneric.Aux[A, B],
      encoder: DerivedEncoder[B, C]
  ): DerivedEncoder[A, C] =
    new DerivedEncoder[A, C] {
      override def encode(value: A): Segments[C] = encoder.encode(generic.to(value))
    }

}

trait DerivedEncoder1 {
  implicit def value[A, K <: Symbol](implicit key: Witness.Aux[K]): DerivedEncoder[FieldType[K, A], A] =
    new DerivedEncoder[FieldType[K, A], A] {
      override def encode(value: FieldType[K, A]): Segments[A] = Segments.one(key.value.name, value)
    }
}
