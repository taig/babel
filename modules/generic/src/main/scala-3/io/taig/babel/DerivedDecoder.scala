package io.taig.babel

import scala.compiletime.*
import scala.deriving.Mirror

trait DerivedDecoder[A] extends Decoder[A]

object DerivedDecoder:
  private def apply[A](f: (Babel, Path) => Either[Decoder.Error, A]): DerivedDecoder[A] =
    new DerivedDecoder[A]:
      def decode(babel: Babel, path: Path): Either[Decoder.Error, A] = f(babel, path)

  private inline def widen[A, B](a: A): A & B = inline a match { case b: B => b }

  private inline def recurse[Names, T <: Tuple](babel: Babel, path: Path): Either[Decoder.Error, T] =
    inline erasedValue[(Names, T)] match {
      case (_: (name *: names), _: (tpe *: types)) =>
        babel match
          case Babel.Object(values) =>
            val segment = constValue[name].toString
            val step = path / segment
            val decoder = summonInline[Decoder[tpe]]

            val left = values.get(segment) match {
              case Some(value) => decoder.decode(value, step)
              case None =>
                decoder.decode(Babel.Null, step).left.map(_ => Decoder.Error("Missing key", step, cause = None))
            }

            for (head <- left; tail <- recurse[names, types](babel, path))
              yield widen[tpe *: types, T](head *: tail)

          case Babel.Value(_) => Left(Decoder.Error.typeMismatch(expected = "Object", actual = "Value", path))
          case Babel.Null     => Left(Decoder.Error.typeMismatch(expected = "Object", actual = "Null", path))
      case (_: EmptyTuple, _) => Right(widen[EmptyTuple, T](EmptyTuple))
    }

  inline given derivedProduct[A](using m: Mirror.ProductOf[A]): DerivedDecoder[A] = DerivedDecoder: (babel, path) =>
    recurse[m.MirroredElemLabels, m.MirroredElemTypes](babel, path).map(m.fromProduct)
