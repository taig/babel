package io.taig.lokal

import cats.data.Chain
import io.circe.Json
import shapeless.{:: => :*:, _}
import shapeless.labelled.FieldType
import simulacrum.typeclass

@typeclass
trait JsonEncoder[A] {
  final def apply(input: A): Json = apply(input, Chain.empty)

  protected def apply(input: A, path: Chain[String]): Json
}

object JsonEncoder {
  implicit val hnil: JsonEncoder[HNil] = (_, _) => Json.obj()

  implicit def hcons[K <: Symbol, H, T <: HList](
      implicit key: Witness.Aux[K],
      head: JsonEncoder[H],
      tail: JsonEncoder[T]
  ): JsonEncoder[FieldType[K, H] :*: T] =
    new JsonEncoder[FieldType[K, H] :*: T] {
      override def apply(input: FieldType[K, H] :*: T, path: Chain[String]): Json =
        head(input.head).deepMerge(tail(input.tail))
    }

  implicit def generic[A, B](implicit generic: LabelledGeneric.Aux[A, B], encoder: => JsonEncoder[B]): JsonEncoder[A] =
    new JsonEncoder[A] {
      override def apply(input: A, path: Chain[String]): Json =
        encoder(generic.to(input), path)
    }

  implicit val dictionary: JsonEncoder[Dictionary] = new JsonEncoder[Dictionary] {
    override def apply(input: Dictionary, path: Chain[String]): Json = {
      ???
    }
  }

  //  implicit def keyEncoderLocale: KeyEncoder[Locale] = KeyEncoder[String].contramap(_.show)
  //
  //  implicit def keyEncoderQuantity: KeyEncoder[Quantity] = KeyEncoder[Int].contramap(_.value)
  //
  //  implicit def encoderTranslation[A, B: Encoder]: Encoder[Translation[A, B]] =
  //    new Encoder[Translation[A, B]] {
  //      override def apply(translation: Translation[A, B]): Json =
  //        encode(translation, minimal = false) match {
  //          case head :: Nil => head
  //          case Nil         => Json.obj()
  //          case elements    => elements.asJson
  //        }
  //    }
  //
  //  private def encode[A, B: Encoder](translation: Translation[A, B], minimal: Boolean): List[Json] =
  //    translation match {
  //      case Translation.Arguments(_, translation) => encode(translation, minimal)
  //      case Translation.Or(left, right) =>
  //        val all = encode(left, minimal = false) ++ encode(right, minimal = false)
  //        val objs = (encode(left, minimal = false) ++ encode(right, minimal = false)).foldLeft(JsonObject.empty) {
  //          (obj, x) =>
  //            x.asObject match {
  //              case Some(value) => obj.deepMerge(value)
  //              case None        => obj
  //            }
  //        }
  //
  //        if (objs.isEmpty) all else objs.asJson +: all.filter(_.isString)
  //      case Translation.Specific(locale, translation) =>
  //        if (minimal) encode(translation, minimal) else List(Json.obj(locale := translation))
  //      case Translation.Universal(value) => List(value.asJson)
  //      case Translation.Plurals(_, _, default, quantities) =>
  //        List(
  //          quantities.toList
  //            .foldLeft(JsonObject("*" -> default.asJson)) {
  //              case (obj, (n, translation)) =>
  //                (n := translation.asJson) +: obj
  //            }
  //            .asJson
  //        )
  //      case Translation.Provide(_, translation) =>
  //        encode(translation, minimal)
  //    }
}
