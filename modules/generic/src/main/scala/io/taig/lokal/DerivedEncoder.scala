package io.taig.lokal

import shapeless._
import shapeless.labelled._

trait DerivedEncoder[A, B] {
  def encode(value: A): Segments[B]
}

object DerivedEncoder {
  def apply[A, B](implicit encoder: DerivedEncoder[A, B]): DerivedEncoder[A, B] = encoder

  implicit def hnil[A]: DerivedEncoder[HNil, A] = new DerivedEncoder[HNil, A] {
    override def encode(value: HNil): Segments[A] = Segments.Empty
  }

  implicit def hcons[K <: Symbol, A, T <: HList](
      implicit
      key: Witness.Aux[K],
      tail: DerivedEncoder[T, A]
  ): DerivedEncoder[FieldType[K, A] :: T, A] = new DerivedEncoder[FieldType[K, A] :: T, A] {
    override def encode(value: FieldType[K, A] :: T): Segments[A] =
      Segments.one(key.value.name, value.head) ++ tail.encode(value.tail)
  }

  implicit def nested[F[_], K <: Symbol, A, T <: HList](
      implicit
      head: Encoder[F, A],
      tail: DerivedEncoder[T, A]
  ): DerivedEncoder[FieldType[K, F[A]] :: T, A] = new DerivedEncoder[FieldType[K, F[A]] :: T, A] {
    override def encode(value: FieldType[K, F[A]] :: T): Segments[A] =
      field[K](head.encode(value.head)) ++ tail.encode(value.tail)
  }

  implicit def optional[K <: Symbol, A, T <: HList](
      implicit
      key: Witness.Aux[K],
      tail: DerivedEncoder[T, A]
  ): DerivedEncoder[FieldType[K, Option[A]] :: T, A] =
    new DerivedEncoder[FieldType[K, Option[A]] :: T, A] {
      override def encode(value: FieldType[K, Option[A]] :: T): Segments[A] =
        value.head.fold[Segments[A]](Segments.Empty)(value => Segments.one(key.value.name, value)) ++ tail.encode(
          value.tail
        )
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
