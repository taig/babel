package io.taig.lokal

import cats.data.Chain
import cats.syntax.all._
import io.circe.{ACursor, Json, JsonObject}
import io.taig.lokal.util.Decoder
import shapeless.labelled.{field, FieldType}
import shapeless.{:: => :*:, _}
import simulacrum.typeclass

@typeclass
trait JsonDecoder[A] {
  final def apply(mode: Mode, strict: Boolean, json: Json): Either[Decoder.Error, A] =
    apply(mode, strict, json, Chain.empty)

  protected def apply(mode: Mode, strict: Boolean, json: Json, path: Chain[String]): Either[Decoder.Error, A]
}

object JsonDecoder {
  implicit val hnil: JsonDecoder[HNil] = (_, _, _, _) => HNil.asRight

  implicit def hcons[K <: Symbol, H, T <: HList](
      implicit key: Witness.Aux[K],
      head: JsonDecoder[H],
      tail: JsonDecoder[T]
  ): JsonDecoder[FieldType[K, H] :*: T] = new JsonDecoder[FieldType[K, H] :*: T] {
    override def apply(
        mode: Mode,
        strict: Boolean,
        json: Json,
        path: Chain[String]
    ): Either[Decoder.Error, FieldType[K, H] :*: T] =
      for {
        head <- head.apply(mode, strict, json, path :+ key.value.name)
        tail <- tail.apply(mode, strict, json, path)
      } yield field[K](head) :: tail
  }

  implicit def generic[A, B](implicit generic: LabelledGeneric.Aux[A, B], decoder: => JsonDecoder[B]): JsonDecoder[A] =
    new JsonDecoder[A] {
      override def apply(mode: Mode, strict: Boolean, json: Json, path: Chain[String]): Either[Decoder.Error, A] =
        decoder.apply(mode, strict, json, path).map(generic.from)
    }

  implicit val dictionary: JsonDecoder[Dictionary] = new JsonDecoder[Dictionary] {
    override def apply(
                        mode: Mode,
                        strict: Boolean,
                        json: Json,
                        path: Chain[String]
                      ): Either[Decoder.Error, Dictionary] = {
      path.foldLeft[ACursor](json.hcursor)(_ downField _).focus match {
        case Some(json) => fromJson(mode, json)
        case None =>
          if (strict) error(s"Missing translation for '${path.mkString_(".")}'")
          else Translation.universal(path.mkString_(".")).asRight
      }
    }

    def fromJson(mode: Mode, json: Json): Either[Decoder.Error, Dictionary] = json.fold(
      unexpectedToken("NULL"),
      _ => unexpectedToken("BOOL"),
      _ => unexpectedToken("NUMBER"),
      fromString(mode, _),
      fromArray(mode, _),
      fromObject(mode, _)
    )

    def fromObject(mode: Mode, obj: JsonObject): Either[Decoder.Error, Dictionary] = mode match {
      case Mode.Monoglot(_) => unexpectedToken("OBJECT")
      case Mode.Polyglot =>
        obj.toList match {
          case head :: Nil => fromTuple(mode, head)
          case head :: tail =>
            fromTuple(mode, head).flatMap { head =>
              tail.foldLeftM[Either[Decoder.Error, *], Dictionary](head)((result, json) =>
                fromTuple(mode, json).map(dictionary => Translation.Or(result, dictionary))
              )
            }
          case Nil => unexpectedToken("EMPTY OBJECT")
        }
    }

    def fromTuple(mode: Mode, t: (String, Json)): Either[Decoder.Error, Dictionary] = Locale.parse(t._1) match {
      case Some(locale) => fromJson(mode, t._2).map(Translation(locale, _))
      case None         => error(s"Invalid locale '${t._1}'")
    }

    def fromArray(mode: Mode, values: Vector[Json]): Either[Decoder.Error, Dictionary] = mode match {
      case Mode.Monoglot(_) => unexpectedToken("ARRAY")
      case Mode.Polyglot =>
        values match {
          case Vector(_) => unexpectedToken("SINGLE ELEMENT ARRAY")
          case Vector()  => unexpectedToken("EMPTY ARRAY")
          case elements =>
            fromJson(mode, elements.head).flatMap { head =>
              elements.tail.foldLeftM[Either[Decoder.Error, *], Dictionary](head) { (result, json) =>
                fromJson(mode, json).map(dictionary => Translation.Or(result, dictionary))
              }
            }
        }
    }

    def fromString(mode: Mode, value: String): Either[Decoder.Error, Dictionary] = mode match {
      case Mode.Monoglot(locale) => Translation(locale, Translation.universal(value)).asRight
      case Mode.Polyglot         => Translation.universal(value).asRight
    }

    def unexpectedToken(name: String): Either[Decoder.Error, Dictionary] = error(s"Unexpected token $name")

    def error(message: String): Either[Decoder.Error, Dictionary] =
      Decoder.Error(s"Failed to decode JSON: $message", None).asLeft
  }
}
