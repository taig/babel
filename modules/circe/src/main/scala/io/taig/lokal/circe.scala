package io.taig.lokal

import cats.implicits._
import io.circe.syntax._
import io.circe.{Decoder, DecodingFailure, Encoder, Json, JsonObject, KeyDecoder, KeyEncoder}

object circe {
  implicit val decoderLocale: Decoder[Locale] =
    Decoder[String].emap(Locale.parseLanguageTag(_).toRight("Invalid Locale"))

  implicit val encoderLocale: Encoder[Locale] = Encoder[String].contramap(_.printLanguageTag)

  implicit val keyDecoderLocale: KeyDecoder[Locale] = KeyDecoder.instance(Locale.parseLanguageTag)

  implicit val keyEncoderLocale: KeyEncoder[Locale] = KeyEncoder.instance(_.printLanguageTag)

  implicit val keyDecoderQuantity: KeyDecoder[Quantity] = KeyDecoder[Int].map(Quantity.apply)

  implicit val keyEncoderQuantity: KeyEncoder[Quantity] = KeyEncoder[Int].contramap(_.value)

  implicit val decoderText: Decoder[Text] = Decoder.instance { cursor =>
    cursor.as[String].map(Text(_, Map.empty)).orElse {
      for {
        default <- cursor.get[String]("*")
        quantities <- cursor.as[JsonObject].map(_.remove("*")).flatMap(Json.fromJsonObject(_).as[Map[Quantity, String]])
      } yield Text(default, quantities)
    }
  }

  implicit val encoderText: Encoder[Text] = Encoder.instance { text =>
    if (text.quantities.isEmpty) text.default.asJson
    else
      Json.obj("*" := text.default) deepMerge text.quantities.asJson
  }

  implicit def decoderPathMap[A: Decoder]: Decoder[Map[Path, A]] = Decoder.instance { cursor =>
    def decode(json: Json, path: Path): Either[DecodingFailure, Map[Path, A]] =
      json.as[A].map(value => Map(path -> value)).orElse {
        json.as[JsonObject].flatMap { obj =>
          obj.keys.foldLeft(Map.empty[Path, A].asRight[DecodingFailure]) {
            case (right @ Right(result), key) =>
              obj.apply(key) match {
                case Some(json) => decode(json, path / key).map(result ++ _)
                case None       => right
              }
            case (failure @ Left(_), _) => failure
          }
        }
      }

    cursor.as[JsonObject].flatMap { obj =>
      obj.toMap.foldLeft(Map.empty[Path, A].asRight[DecodingFailure]) {
        case (Right(result), (key, json)) => decode(json, Path.one(key)).map(result ++ _)
        case (failure @ Left(_), _)       => failure
      }
    }
  }

  implicit def encoderPathMap[A: Encoder]: Encoder[Map[Path, A]] = Encoder.instance { values =>
    def encode(path: Path, value: A): Json = path match {
      case Path(head, Nil)             => Json.obj(head := value)
      case Path(segment, head :: tail) => Json.obj(segment := encode(Path(head, tail), value))
    }

    values.map((encode _).tupled).foldLeft(Json.obj())(_ deepMerge _)
  }

  implicit val decoderDictionary: Decoder[Dictionary] = Decoder[Map[Path, Text]].map(Dictionary.apply)

  implicit val encoderDictionary: Encoder[Dictionary] = Encoder[Map[Path, Text]].contramap(_.values)

  implicit val keyEncoderEitherLocale: KeyEncoder[Either["*", Locale]] = KeyEncoder.instance {
    case Right(locale) => locale.printLanguageTag
    case Left(_)       => "*"
  }

  implicit val decoderTranslations: Decoder[Translation] = Decoder.instance { cursor =>
    for {
      values <- cursor.as[JsonObject].map(_.remove("*")).flatMap(Json.fromJsonObject(_).as[Map[Locale, Text]])
      fallback <- cursor.get[Option[Text]]("*")
    } yield Translation(values, fallback)
  }

  implicit val encoderTranslations: Encoder[Translation] = Encoder[Map[String, Text]].contramap { translation =>
    translation.values.map(_.leftMap(_.printLanguageTag)) ++ translation.fallback.map("*" -> _).toMap
  }

  implicit val decoderI18n: Decoder[I18n] = Decoder[Map[Path, Translation]].map(I18n.apply)

  implicit val encoderI18n: Encoder[I18n] = Encoder[Map[Path, Translation]].contramap(_.values)
}
