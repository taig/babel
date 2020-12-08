package io.taig.lokal

import cats.syntax.all._
import io.circe.syntax._
import io.circe.{Json, JsonObject, KeyEncoder}
import shapeless.labelled.FieldType
import shapeless.{:: => :*:, _}
import simulacrum.typeclass

@typeclass
trait JsonEncoder[A] {
  def apply(input: A): Json
}

object JsonEncoder {
  implicit val hnil: JsonEncoder[HNil] = _ => Json.obj()

  implicit def hcons[K <: Symbol, H, T <: HList](
      implicit key: Witness.Aux[K],
      head: JsonEncoder[H],
      tail: JsonEncoder[T]
  ): JsonEncoder[FieldType[K, H] :*: T] =
    input => Json.obj(key.value.name := head(input.head)) deepMerge tail(input.tail)

  implicit def generic[A, B](implicit generic: LabelledGeneric.Aux[A, B], encoder: => JsonEncoder[B]): JsonEncoder[A] =
    input => encoder(generic.to(input))

  implicit val dictionary: JsonEncoder[Dictionary] = new JsonEncoder[Dictionary] {
    override def apply(dictionary: Dictionary): Json = apply(dictionary, minimal = false)

    def apply(dictionary: Dictionary, minimal: Boolean): Json =
      fromDictionary(dictionary, minimal) match {
        case head :: Nil => head
        case Nil         => Json.obj()
        case elements    => Json.arr(elements: _*)
      }

    implicit val keyEncoderLocale: KeyEncoder[Locale] = KeyEncoder[String].contramap(_.show)

    implicit val keyEncoderQuantity: KeyEncoder[Quantity] = KeyEncoder[Int].contramap(_.value)

    def fromDictionary[A, B](dictionary: Dictionary, minimal: Boolean): List[Json] =
      dictionary match {
        case Translation.Or(left, right) =>
          fromDictionary(left, minimal = false) ++ fromDictionary(right, minimal = false)
        case Translation.Specific(locale, translation) =>
          if (minimal) fromDictionary(translation, minimal) else List(Json.obj(locale := apply(translation, minimal)))
        case Translation.Universal(value)          => List(value.asJson)
        case Translation.Arguments(_, translation) => List(apply(translation, minimal))
        case Translation.Plurals(_, _, default, quantities) =>
          if (quantities.isEmpty) List(apply(default, minimal))
          else
            Json.obj("*" -> apply(default, minimal)) +: quantities.map {
              case (quantity, json) => Json.obj(quantity := apply(json, minimal))
            }.toList
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
