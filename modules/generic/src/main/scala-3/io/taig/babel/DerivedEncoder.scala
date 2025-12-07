package io.taig.babel

import scala.compiletime._
import scala.deriving.Mirror

trait DerivedEncoder[A] extends Encoder[A]

object DerivedEncoder:
  private def apply[A](f: A => Babel): DerivedEncoder[A] = new DerivedEncoder[A]:
    override def encode(a: A): Babel = f(a)

  private inline def recurse[Names, Types](element: Product, idx: Int): Babel =
    inline erasedValue[(Names, Types)] match
      case (_: (name *: names), _: (tpe *: types)) =>
        val key = constValue[name].toString
        val value = element.productElement(idx).asInstanceOf[tpe]
        val encoder = summonInline[Encoder[tpe]]

        recurse[names, types](element, idx + 1) match {
          case Babel.Object(values) => Babel.Object(Map(key -> encoder.encode(value)) ++ values)
          case _                    => Babel.one(key, encoder.encode(value))
        }

      case (_: EmptyTuple, _) => Babel.Null

  inline given derivedProduct[T](using m: Mirror.ProductOf[T]): DerivedEncoder[T] = DerivedEncoder: a =>
    recurse[m.MirroredElemLabels, m.MirroredElemTypes](a.asInstanceOf[Product], 0)
