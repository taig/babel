package net.slozzer.babel

import shapeless.labelled.{field, FieldType}
import shapeless.{::, HList, HNil, LabelledGeneric, Witness}
import simulacrum.typeclass

@typeclass
trait DerivedDecoder[A] extends Decoder[A]

object DerivedDecoder extends DerivedDecoder1 {
  implicit val hnil: DerivedDecoder[HNil] = (_, _) => Right(HNil)

  implicit def hcons[K <: Symbol, A, T <: HList](implicit
      key: Witness.Aux[K],
      head: DerivedDecoder[A],
      tail: DerivedDecoder[T]
  ): DerivedDecoder[FieldType[K, A] :: T] = new DerivedDecoder[FieldType[K, A] :: T] {
    override def decode(babel: Babel, path: Path): Either[Decoder.Error, FieldType[K, A] :: T] = {
      val segment = key.value.name

      babel match {
        case Babel.Object(values) =>
          values.get(segment) match {
            case Some(value) =>
              for {
                head <- head.decode(value, path / segment)
                tail <- tail.decode(babel, path / segment)
              } yield field[K](head) :: tail
            case None => Left(Decoder.Error("Missing key", path / segment, cause = None))
          }
        case Babel.Value(_) => Left(Decoder.Error.typeMismatch(expected = "Object", actual = "Value", path / segment))
      }
    }
  }
//
  //  implicit def nested[F[_], K <: Symbol, A, T <: HList](implicit
//      key: Witness.Aux[K],
//      decoder: Decoder[F, A],
//      tail: DerivedDecoder[A, T]
//  ): DerivedDecoder[A, FieldType[K, F[A]] :: T] = new DerivedDecoder[A, FieldType[K, F[A]] :: T] {
//    override def decode(path: Path, segments: Segments[A]): Either[Decoder.Error, FieldType[K, F[A]] :: T] = {
//      val segment = key.value.name
//
//      val head = segments.get(segment) match {
//        case Some(Right(segments)) => decoder.decode(path / segment, segments)
//        case Some(Left(_))         => Left(Decoder.Error(s"Expected segments, got value (${(path / segment).print})"))
//        case None                  => Left(Decoder.Error(s"No segments for key (${(path / segment).printPretty})"))
//      }
//
//      for {
//        head <- head
//        tail <- tail.decode(path, segments)
//      } yield field[K](head) :: tail
//    }
//  }
//
//  implicit def optional[K <: Symbol, A, T <: HList](implicit
//      key: Witness.Aux[K],
//      tail: DerivedDecoder[A, T]
//  ): DerivedDecoder[A, FieldType[K, Option[A]] :: T] = new DerivedDecoder[A, FieldType[K, Option[A]] :: T] {
//    override def decode(path: Path, segments: Segments[A]): Either[Decoder.Error, FieldType[K, Option[A]] :: T] = {
//      val segment = key.value.name
//
//      val head = segments.get(segment) match {
//        case Some(Left(value)) => Right(field[K](Some(value)))
//        case Some(Right(_))    => Left(Decoder.Error(s"Expected value, got segments (${(path / segment).print})"))
//        case None              => Right(field[K](None))
//      }
//
//      for {
//        head <- head
//        tail <- tail.decode(path, segments)
//      } yield head :: tail
//    }
//  }

  implicit def decoderLabelledGeneric[A, B <: HList](implicit
      generic: LabelledGeneric.Aux[A, B],
      decoder: DerivedDecoder[B]
  ): DerivedDecoder[A] = new DerivedDecoder[A] {
    override def decode(babel: Babel, path: Path): Either[Decoder.Error, A] =
      decoder.decode(babel, path).map(generic.from)
  }
}

trait DerivedDecoder1 {
  implicit def decoder[A](implicit decoder: Decoder[A]): DerivedDecoder[A] = new DerivedDecoder[A] {
    override def decode(babel: Babel, path: Path): Either[Decoder.Error, A] = decoder.decode(babel, path)
  }
}
