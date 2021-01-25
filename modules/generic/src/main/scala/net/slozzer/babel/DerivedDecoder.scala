package net.slozzer.babel

import shapeless.labelled.{field, FieldType}
import shapeless.{::, HList, HNil, LabelledGeneric, Witness}
import simulacrum.typeclass

@typeclass
trait DerivedDecoder[A] extends Decoder[A]

object DerivedDecoder {
  implicit val hnil: DerivedDecoder[HNil] = (_, _) => Right(HNil)

  implicit def hcons[K <: Symbol, A, T <: HList](implicit
      key: Witness.Aux[K],
      head: Decoder[A],
      tail: DerivedDecoder[T]
  ): DerivedDecoder[FieldType[K, A] :: T] = {
    case (babel @ Babel.Object(values), path) =>
      val segment = key.value.name

      values.get(segment) match {
        case Some(value) =>
          for {
            head <- head.decode(value, path / segment)
            tail <- tail.decode(babel, path / segment)
          } yield field[K](head) :: tail
        case None => Left(Decoder.Error("Missing key", path / segment, cause = None))
      }
    case (Babel.Value(_), path) => Left(Decoder.Error.typeMismatch(expected = "Object", actual = "Value", path))
  }

  implicit def decoderLabelledGeneric[A, B <: HList](implicit
      generic: LabelledGeneric.Aux[A, B],
      decoder: DerivedDecoder[B]
  ): DerivedDecoder[A] = (babel, path) => decoder.decode(babel, path).map(generic.from)
}
