package io.taig.babel

final case class Quantities[A](default: A, quantities: List[Quantities.Element[A]]) {
  def apply(quantity: Int): A = quantities.find(_.quantity.matches(quantity)).map(_.value).getOrElse(default)

  def map[B](f: A => B): Quantities[B] = Quantities(f(default), quantities.map(_.map(f)))

  def mapWithQuantity[B](f: (Option[Quantity], A) => B): Quantities[B] =
    Quantities(f(None, default), quantities.map(_.mapWithQuantity((quantity, value) => f(Some(quantity), value))))

  def ++(quantities: Quantities[A]): Quantities[A] =
    Quantities(quantities.default, this.quantities ++ quantities.quantities)

  override def toString: String =
    if (quantities.isEmpty) s""""$default"""" else s"""{${quantities.mkString(", ")}, *: "$default"}"""
}

object Quantities {
  val Wildcard: String = "*"

  final case class Element[A](quantity: Quantity, value: A) {
    def map[B](f: A => B): Element[B] = copy(value = f(value))

    def as[B](value: B): Element[B] = map(_ => value)

    def mapWithQuantity[B](f: (Quantity, A) => B): Element[B] = copy(value = f(quantity, value))

    override def toString: String = s"""$quantity: "$value""""
  }

  def from[A](default: A, quantities: Iterable[Quantities.Element[A]]): Quantities[A] =
    Quantities(default, quantities.toList)

  def of[A](default: A, quantities: Quantities.Element[A]*): Quantities[A] = from(default, quantities)

  implicit def encoder[A: Encoder]: Encoder[Quantities[A]] = Encoder[Map[String, A]].contramap { quantities =>
    Map(Wildcard -> quantities.default) ++
      quantities.quantities.map(element => element.quantity.toString -> element.value).toMap
  }

  implicit def decoder[A: Decoder]: Decoder[Quantities[A]] = Decoder[Map[String, A]].emap { (values, path) =>
    values.get(Wildcard) match {
      case Some(default) =>
        (values - Wildcard)
          .foldLeft[Either[Decoder.Error, List[Element[A]]]](Right(List.empty[Element[A]])) {
            case (Right(quantities), (key, value)) =>
              Quantity
                .parse(key)
                .left
                .map(reason => Decoder.Error(s"Invalid Quantity: $reason", path / key, cause = None))
                .map(quantity => quantities :+ Element(quantity, value))
            case (left @ Left(_), _) => left.asInstanceOf[Either[Decoder.Error, List[Element[A]]]]
          }
          .map(Quantities(default, _))
      case None => Left(Decoder.Error("Missing key: *", path, cause = None))
    }
  }
}
