package net.slozzer.babel

import scala.util.Try

final case class Quantities[A](default: A, quantities: Map[Quantity, A]) {
  def apply(quantity: Int): A = quantities.getOrElse(Quantity(quantity), default)

  override def toString: String = if (quantities.isEmpty) s""""$default""""
  else {
    val details = quantities.map { case (quantity, value) => s"""${quantity.value}: "$value"""" }.mkString(", ")
    s"""{$details, *: "$default"}"""
  }
}

object Quantities {
  def one[A](value: A): Quantities[A] = Quantities(value, Map.empty)

  implicit def encoder[A: Encoder]: Encoder[Quantities[A]] = Encoder[Map[String, A]].contramap { quantities =>
    Map("*" -> quantities.default) ++
      quantities.quantities.map { case (quantity, value) => String.valueOf(quantity.value) -> value }
  }

  implicit def decoder[A: Decoder]: Decoder[Quantities[A]] = Decoder[Map[String, A]].emap {
    (values, path) =>
      values.get("*") match {
        case Some(default) =>
          (values - "*").foldLeft[Either[Decoder.Error, Map[Quantity, A]]](Right(Map.empty[Quantity, A])) {
            case (Right(quantities), (key, value)) =>
              Try(key.toInt)
                .toEither
                .left
                .map(cause => Decoder.Error("Invalid key", path / key, Some(cause)))
                .map(quantity => quantities + (Quantity(quantity) -> value))
            case (left@Left(_), _) => left.asInstanceOf[Either[Decoder.Error, Map[Quantity, A]]]
          }.map(Quantities(default, _))
        case None => Left(Decoder.Error("Missing key: *", path, cause = None))
      }
  }
}
