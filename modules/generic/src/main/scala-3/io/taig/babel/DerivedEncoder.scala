package io.taig.babel

import scala.compiletime.*
import scala.deriving.Mirror
import scala.annotation.nowarn

trait DerivedEncoder[A] extends Encoder[A] {
  override def encode(value: A): Babel
}

object DerivedEncoder {

  private inline def recurse[Names, Types](element: Product, idx: Int): Babel =
    inline erasedValue[(Names, Types)] match {
      case (_: (name *: names), _: (tpe *: types)) =>
        val key = constValue[name].toString
        val value = element.productElement(idx).asInstanceOf[tpe]
        val encoder = summonInline[Encoder[tpe]]

        recurse[names, types](element, idx + 1) match {
          case Babel.Object(values) => Babel.Object(Map(key -> encoder.encode(value)) ++ values)
          case _                    => Babel.one(key, encoder.encode(value))
        }

      case (_: EmptyTuple, _) => Babel.Null
    }

  @nowarn("msg=An inline given alias with a function value")
  inline given derivedProduct[T](using m: Mirror.ProductOf[T]): DerivedEncoder[T] =
    (value: T) => recurse[m.MirroredElemLabels, m.MirroredElemTypes](value.asInstanceOf[Product], 0)
}
