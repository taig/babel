package io.taig.lokal

import io.circe.{Encoder, Json, JsonObject, KeyEncoder}
import io.circe.syntax._
import cats.syntax.all._

object circe {
  implicit def keyEncoderLocale: KeyEncoder[Locale] = KeyEncoder[String].contramap(_.show)

  implicit def keyEncoderQuantity: KeyEncoder[Quantity] = KeyEncoder[Int].contramap(_.value)

  implicit def encoderTranslation[A, B: Encoder]: Encoder[Translation[A, B]] =
    new Encoder[Translation[A, B]] {
      override def apply(translation: Translation[A, B]): Json =
        encode(translation, minimal = false) match {
          case head :: Nil => head
          case Nil         => Json.obj()
          case elements    => elements.asJson
        }
    }

  private def encode[A, B: Encoder](translation: Translation[A, B], minimal: Boolean): List[Json] =
    translation match {
      case Translation.Arguments(_, translation) => encode(translation, minimal)
      case Translation.Or(left, right) =>
        val all = encode(left, minimal = false) ++ encode(right, minimal = false)
        val objs = (encode(left, minimal = false) ++ encode(right, minimal = false)).foldLeft(JsonObject.empty) {
          (obj, x) =>
            x.asObject match {
              case Some(value) => obj.deepMerge(value)
              case None        => obj
            }
        }

        if (objs.isEmpty) all else objs.asJson +: all.filter(_.isString)
      case Translation.Specific(locale, translation) =>
        if (minimal) encode(translation, minimal) else List(Json.obj(locale := translation))
      case Translation.Universal(value) => List(value.asJson)
      case Translation.Plurals(_, _, default, quantities) =>
        List(
          quantities.toList
            .foldLeft(JsonObject("*" -> default.asJson)) {
              case (obj, (n, translation)) =>
                (n := translation.asJson) +: obj
            }
            .asJson
        )
      case Translation.Provide(_, translation) =>
        encode(translation, minimal)
    }
}
