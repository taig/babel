package io.taig.babel

import shapeless._
import shapeless.labelled.FieldType
import simulacrum.typeclass

import scala.annotation.nowarn

@nowarn("msg=Unused import")
@typeclass
trait DerivedEncoder[A] extends Encoder[A] {
  override def encode(value: A): Babel
}

object DerivedEncoder {
  implicit val hnil: DerivedEncoder[HNil] = _ => Babel.Null

  implicit def hcons[K <: Symbol, A, T <: HList](implicit
      key: Witness.Aux[K],
      head: Encoder[A],
      tail: DerivedEncoder[T]
  ): DerivedEncoder[FieldType[K, A] :: T] = (value: FieldType[K, A] :: T) =>
    tail.encode(value.tail) match {
      case Babel.Object(values) => Babel.Object(Map(key.value.name -> head.encode(value.head)) ++ values)
      case _                    => Babel.one(key.value.name, head.encode(value.head))
    }

  implicit def encoderLabelledGeneric[A, B <: HList, C](implicit
      generic: LabelledGeneric.Aux[A, B],
      encoder: DerivedEncoder[B]
  ): DerivedEncoder[A] = value => encoder.encode(generic.to(value))
}
