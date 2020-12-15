package io.taig.lokal

import cats.syntax.all._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json, JsonObject, KeyDecoder, KeyEncoder}

object circe {
  implicit val decoderLocale: Decoder[Locale] = Decoder[String].emap(Locale.parse(_).toRight("Invalid Locale"))

  implicit val encoderLocale: Encoder[Locale] = Encoder[String].contramap(_.show)

  implicit val keyDecoderLocale: KeyDecoder[Locale] = KeyDecoder.instance(Locale.parse)

  implicit val keyEncoderLocale: KeyEncoder[Locale] = KeyEncoder.instance(_.show)

  implicit val keyDecoderQuantity: KeyDecoder[Quantity] = KeyDecoder[Int].map(Quantity.apply)

  implicit val keyEncoderQuantity: KeyEncoder[Quantity] = KeyEncoder[Int].contramap(_.value)

  implicit val decoderText: Decoder[Text] = Decoder.instance { cursor =>
    cursor.as[String].map(Text(_, Map.empty)).orElse {
      for {
        default <- cursor.get[String]("default")
        quantities <- cursor.get[Map[Quantity, String]]("quantities")
      } yield Text(default, quantities)
    }
  }

  implicit val encoderText: Encoder[Text] = Encoder.instance { text =>
    if (text.quantities.isEmpty) text.default.asJson
    else
      Json.obj(
        "default" := text.default,
        "quantities" := text.quantities
      )
  }

  implicit val decoderDictionary: Decoder[Dictionary] = Decoder[Map[String, Text]].map(Dictionary.apply)

  implicit val encoderDictionary: Encoder[Dictionary] = Encoder[Map[String, Text]].contramap(_.values)

  implicit val keyEncoderEitherLocale: KeyEncoder[Either["*", Locale]] = KeyEncoder.instance {
    case Right(locale) => locale.show
    case Left(_)       => "*"
  }

  implicit val decoderTranslations: Decoder[Translation] = Decoder.instance { cursor =>
    for {
      values <- cursor.as[JsonObject].map(_.remove("*")).flatMap(Json.fromJsonObject(_).as[Map[Locale, Text]])
      fallback <- cursor.get[Option[Text]]("*")
    } yield Translation(values, fallback)
  }

  implicit val encoderTranslations: Encoder[Translation] = Encoder[Map[String, Text]].contramap { translation =>
    translation.values.map(_.leftMap(_.show)) ++ translation.fallback.map("*" -> _).toMap
  }

  implicit val decoderI18n: Decoder[I18n] = Decoder[Map[String, Translation]].map(I18n.apply)

  implicit val encoderI18n: Encoder[I18n] = Encoder[Map[String, Translation]].contramap(_.values)
}
