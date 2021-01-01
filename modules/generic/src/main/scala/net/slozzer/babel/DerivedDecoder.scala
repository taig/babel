package net.slozzer.babel

import shapeless.{::, HList, HNil, LabelledGeneric, Witness}
import shapeless.labelled._

trait DerivedDecoder[A, B] {
  def decode(path: Path, segments: Segments[A]): Either[Decoder.Error, B]
}

object DerivedDecoder {
  def apply[A, B](implicit decoder: DerivedDecoder[A, B]): DerivedDecoder[A, B] = decoder

  implicit def hnil[A]: DerivedDecoder[A, HNil] = new DerivedDecoder[A, HNil] {
    override def decode(path: Path, segments: Segments[A]): Either[Decoder.Error, HNil] = Right(HNil)
  }

  implicit def hcons[K <: Symbol, A, T <: HList](implicit
      key: Witness.Aux[K],
      tail: DerivedDecoder[A, T]
  ): DerivedDecoder[A, FieldType[K, A] :: T] = new DerivedDecoder[A, FieldType[K, A] :: T] {
    override def decode(path: Path, segments: Segments[A]): Either[Decoder.Error, FieldType[K, A] :: T] = {
      val segment = key.value.name

      val head = segments.get(segment) match {
        case Some(Left(value)) => Right(field[K](value))
        case Some(Right(_))    => Left(Decoder.Error(s"Expected value, got segments (${(path / segment).print})"))
        case None              => Left(Decoder.Error(s"No value for key (${(path / segment).printPretty})"))
      }

      for {
        head <- head
        tail <- tail.decode(path, segments)
      } yield head :: tail
    }
  }

  implicit def nested[F[_], K <: Symbol, A, T <: HList](implicit
      key: Witness.Aux[K],
      decoder: Decoder[F, A],
      tail: DerivedDecoder[A, T]
  ): DerivedDecoder[A, FieldType[K, F[A]] :: T] = new DerivedDecoder[A, FieldType[K, F[A]] :: T] {
    override def decode(path: Path, segments: Segments[A]): Either[Decoder.Error, FieldType[K, F[A]] :: T] = {
      val segment = key.value.name

      val head = segments.get(segment) match {
        case Some(Right(segments)) => decoder.decode(path / segment, segments)
        case Some(Left(_))         => Left(Decoder.Error(s"Expected segments, got value (${(path / segment).print})"))
        case None                  => Left(Decoder.Error(s"No segments for key (${(path / segment).printPretty})"))
      }

      for {
        head <- head
        tail <- tail.decode(path, segments)
      } yield field[K](head) :: tail
    }
  }

  implicit def optional[K <: Symbol, A, T <: HList](implicit
      key: Witness.Aux[K],
      tail: DerivedDecoder[A, T]
  ): DerivedDecoder[A, FieldType[K, Option[A]] :: T] = new DerivedDecoder[A, FieldType[K, Option[A]] :: T] {
    override def decode(path: Path, segments: Segments[A]): Either[Decoder.Error, FieldType[K, Option[A]] :: T] = {
      val segment = key.value.name

      val head = segments.get(segment) match {
        case Some(Left(value)) => Right(field[K](Some(value)))
        case Some(Right(_))    => Left(Decoder.Error(s"Expected value, got segments (${(path / segment).print})"))
        case None              => Right(field[K](None))
      }

      for {
        head <- head
        tail <- tail.decode(path, segments)
      } yield head :: tail
    }
  }

  implicit def decoderLabelledGeneric[A, B, C <: HList](implicit
      generic: LabelledGeneric.Aux[B, C],
      decoder: DerivedDecoder[A, C]
  ): DerivedDecoder[A, B] = new DerivedDecoder[A, B] {
    override def decode(path: Path, segments: Segments[A]): Either[Decoder.Error, B] =
      decoder.decode(path, segments).map(generic.from)
  }
}
