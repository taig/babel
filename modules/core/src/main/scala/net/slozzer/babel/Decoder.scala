package net.slozzer.babel

import simulacrum.typeclass

import scala.util.Try

@typeclass
trait Decoder[A] {
  final def decode(babel: Babel): Either[Decoder.Error, A] = decode(babel, Path.Root)

  protected[babel] def decode(babel: Babel, path: Path): Either[Decoder.Error, A]

  final def decodeAll(translations: Translations[Babel]): Either[Decoder.Error, Translations[A]] =
    translations.values.foldLeft[Either[Decoder.Error, Translations[A]]](Right(Translations.Empty)) {
      case (Right(translations), (locale, babel)) =>
        decode(babel, Path.Root) match {
          case Right(value)   => Right(translations + (locale -> value))
          case left @ Left(_) => left.asInstanceOf[Either[Decoder.Error, Translations[A]]]
        }
      case (left @ Left(_), _) => left
    }

  final def map[B](f: A => B): Decoder[B] = (babel, path) => decode(babel, path).map(f)

  final def emap[B](f: (A, Path) => Either[Decoder.Error, B]): Decoder[B] = (babel, path) =>
    decode(babel, path).flatMap(f(_, path))
}

object Decoder {
  final case class Error(message: String, path: Path, cause: Option[Throwable])
      extends Exception(s"Failed to decode: $message ${path.printPlaceholder}", cause.orNull)

  object Error {
    def typeMismatch(expected: String, actual: String, path: Path): Error =
      Error(s"Type mismatch: expected $expected, got $actual", path, cause = None)
  }

  implicit def map[A](implicit decoder: Decoder[A]): Decoder[Map[String, A]] = {
    case (Babel.Object(values), path) =>
      values.foldLeft[Either[Decoder.Error, Map[String, A]]](Right(Map.empty[String, A])) {
        case (Right(result), (key, babel)) =>
          decoder.decode(babel, path / key) match {
            case Right(value)   => Right(result + (key -> value))
            case left @ Left(_) => left.asInstanceOf[Either[Decoder.Error, Map[String, A]]]
          }
        case (left @ Left(_), _) => left
      }
    case (Babel.Value(_), path) => Left(Error.typeMismatch(expected = "Object", actual = "Value", path))
  }

  implicit val string: Decoder[String] = {
    case (Babel.Value(value), _) => Right(value)
    case (Babel.Object(_), path) => Left(Error.typeMismatch(expected = "Value", actual = "Object", path))
  }

  def numeric[A](f: String => Try[A], tpe: String): Decoder[A] = Decoder[String].emap { (value, path) =>
    f(value).toEither.left.map { throwable =>
      Error(s"Invalid numeric conversion to $tpe", path, cause = Some(throwable))
    }
  }

  implicit val double: Decoder[Double] = numeric(value => Try(value.toDouble), "Double")

  implicit val float: Decoder[Float] = numeric(value => Try(value.toFloat), "Float")

  implicit val int: Decoder[Int] = numeric(value => Try(value.toInt), "Int")

  implicit val long: Decoder[Long] = numeric(value => Try(value.toLong), "Long")

  implicit val short: Decoder[Short] = numeric(value => Try(value.toShort), "Short")
}
