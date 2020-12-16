package io.taig.lokal

import shapeless._
import shapeless.labelled._

trait DerivedDecoder[A, B] {
  def decode(segments: Segments[A]): Either[Decoder.Error, B]
}

object DerivedDecoder extends DerivedDecoder1 {
  def apply[A, B](implicit decoder: DerivedDecoder[A, B]): DerivedDecoder[A, B] = decoder

  implicit def segments[A, K <: Symbol, V](
      implicit key: Witness.Aux[K],
      decoder: DerivedDecoder[A, V]
  ): DerivedDecoder[A, FieldType[K, V]] =
    new DerivedDecoder[A, FieldType[K, V]] {
      override def decode(segments: Segments[A]): Either[Decoder.Error, FieldType[K, V]] =
        segments.get(key.value.name) match {
          case Some(Right(segments)) => decoder.decode(segments).map(field[K](_))
          case Some(Left(_))         => Left(Decoder.Error("Expected segments, got value"))
          case None                  => Left(Decoder.Error(s"No value for key ${key.value.name}"))
        }
    }

  implicit def optional[A, K <: Symbol, V](
      implicit key: Witness.Aux[K],
      decoder: DerivedDecoder[A, FieldType[K, V]]
  ): DerivedDecoder[A, FieldType[K, Option[V]]] =
    new DerivedDecoder[A, FieldType[K, Option[V]]] {
      override def decode(segments: Segments[A]): Either[Decoder.Error, FieldType[K, Option[V]]] =
        segments.get(key.value.name) match {
          case Some(_) => decoder.decode(segments).map(result => field[K](Some(result)))
          case None    => Right(field[K](None))
        }
    }

  implicit def hnil[A]: DerivedDecoder[A, HNil] = new DerivedDecoder[A, HNil] {
    override def decode(segments: Segments[A]): Either[Decoder.Error, HNil] = Right(HNil)
  }

  implicit def hcons[A, K <: Symbol, V, T <: HList](
      implicit
      head: DerivedDecoder[A, FieldType[K, V]],
      tail: DerivedDecoder[A, T]
  ): DerivedDecoder[A, FieldType[K, V] :: T] = new DerivedDecoder[A, FieldType[K, V] :: T] {
    override def decode(segments: Segments[A]): Either[Decoder.Error, FieldType[K, V] :: T] =
      for {
        head <- head.decode(segments)
        tail <- tail.decode(segments)
      } yield head :: tail
  }

  implicit def decoderLabelledGeneric[A, B, C <: HList](
      implicit generic: LabelledGeneric.Aux[B, C],
      decoder: DerivedDecoder[A, C]
  ): DerivedDecoder[A, B] =
    new DerivedDecoder[A, B] {
      override def decode(segments: Segments[A]): Either[Decoder.Error, B] =
        decoder.decode(segments).map(generic.from)
    }
}

trait DerivedDecoder1 {
  implicit def value[A, K <: Symbol](implicit key: Witness.Aux[K]): DerivedDecoder[A, FieldType[K, A]] =
    new DerivedDecoder[A, FieldType[K, A]] {
      override def decode(segments: Segments[A]): Either[Decoder.Error, FieldType[K, A]] =
        segments.get(key.value.name) match {
          case Some(Left(value)) => Right(field[K](value))
          case Some(Right(_))    => Left(Decoder.Error("Expected value, got segments"))
          case None              => Left(Decoder.Error(s"No value for key ${key.value.name}"))
        }
    }
}
