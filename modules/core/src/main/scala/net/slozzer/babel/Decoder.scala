package net.slozzer.babel

trait Decoder[A] {
  final def decode(babel: Babel): Either[Decoder.Error, A] = decode(babel, Path.Root)

  protected[babel] def decode(babel: Babel, path: Path): Either[Decoder.Error, A]

  final def decodeAll(translations: Translations[Babel]): Either[Decoder.Error, Translations[A]] =
    translations.toList.foldLeft[Either[Decoder.Error, Translations[A]]](Right(Translations.Empty)) {
      case (Right(translations), translation) =>
        decode(translation.value, Path.Root) match {
          case Right(value)   => Right(translations + translation.as(value))
          case left @ Left(_) => left.asInstanceOf[Either[Decoder.Error, Translations[A]]]
        }
      case (left @ Left(_), _) => left
    }

  final def map[B](f: A => B): Decoder[B] = (babel, path) => decode(babel, path).map(f)

  final def emap[B](f: (A, Path) => Either[Decoder.Error, B]): Decoder[B] = (babel, path) =>
    decode(babel, path).flatMap(f(_, path))
}

object Decoder {
  def apply[A](implicit decoder: Decoder[A]): Decoder[A] = decoder

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
    case (Babel.Null, path)     => Left(Error.typeMismatch(expected = "Object", actual = "Null", path))
  }

  implicit val string: Decoder[String] = {
    case (Babel.Value(value), _) => Right(value)
    case (Babel.Object(_), path) => Left(Error.typeMismatch(expected = "Value", actual = "Object", path))
    case (Babel.Null, path)      => Left(Error.typeMismatch(expected = "Value", actual = "Null", path))
  }

  implicit def option[A](implicit decoder: Decoder[A]): Decoder[Option[A]] = {
    case (Babel.Null, _) => Right(None)
    case (babel, path)   => decoder.decode(babel, path).map(Some.apply)
  }

  def numeric[A](f: String => Option[A], tpe: String): Decoder[A] = Decoder[String].emap { (value, path) =>
    f(value).toRight(Error(s"Invalid numeric conversion to $tpe", path, cause = None))
  }

  implicit val double: Decoder[Double] = numeric(value => value.toDoubleOption, "Double")

  implicit val float: Decoder[Float] = numeric(value => value.toFloatOption, "Float")

  implicit val int: Decoder[Int] = numeric(value => value.toIntOption, "Int")

  implicit val long: Decoder[Long] = numeric(value => value.toLongOption, "Long")

  implicit val short: Decoder[Short] = numeric(value => value.toShortOption, "Short")
}
