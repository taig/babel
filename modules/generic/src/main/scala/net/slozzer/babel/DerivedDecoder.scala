package net.slozzer.babel

import shapeless.labelled.{field, FieldType}
import shapeless.{::, HList, HNil, LabelledGeneric, Witness}

trait DerivedDecoder[A] extends Decoder[A]

object DerivedDecoder {
  def apply[A](implicit decoder: DerivedDecoder[A]): DerivedDecoder[A] = decoder

  implicit val hnil: DerivedDecoder[HNil] = (_, _) => Right(HNil)

  implicit def hcons[K <: Symbol, A, T <: HList](implicit
      key: Witness.Aux[K],
      head: Decoder[A],
      tail: DerivedDecoder[T]
  ): DerivedDecoder[FieldType[K, A] :: T] = {
    case (babel @ Babel.Object(values), path) =>
      val segment = key.value.name
      val step = path / segment

      val left = values.get(segment) match {
        case Some(value) => head.decode(value, step)
        case None        => head.decode(Babel.Null, step).left.map(_ => Decoder.Error("Missing key", step, cause = None))
      }

      for {
        head <- left
        tail <- tail.decode(babel, path)
      } yield field[K](head) :: tail
    case (Babel.Value(_), path) => Left(Decoder.Error.typeMismatch(expected = "Object", actual = "Value", path))
    case (Babel.Null, path)     => Left(Decoder.Error.typeMismatch(expected = "Object", actual = "Null", path))
  }

  implicit def decoderLabelledGeneric[A, B <: HList](implicit
      generic: LabelledGeneric.Aux[A, B],
      decoder: DerivedDecoder[B]
  ): DerivedDecoder[A] = (babel, path) => decoder.decode(babel, path).map(generic.from)
}
