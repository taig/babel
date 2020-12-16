package io.taig.babel

import cats.implicits._
import io.circe.syntax._
import io.circe.parser._
import io.circe.{Decoder, DecodingFailure, Encoder, Json, JsonObject, KeyDecoder, KeyEncoder}
import io.circe.{Printer => CircePrinter}

object circe {
  implicit def keyDecoderParser[A: Parser]: KeyDecoder[A] = KeyDecoder.instance(Parser[A].parse(_).toOption)

  implicit def keyEncoderPrinter[A: Printer]: KeyEncoder[A] = KeyEncoder.instance(Printer[A].print)

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
    else Json.obj("*" := text.default) deepMerge text.quantities.asJson
  }

  implicit def decoderSegments[A: Decoder]: Decoder[Segments[A]] = Decoder.instance { cursor =>
    cursor
      .as[JsonObject]
      .flatMap { obj =>
        obj.toMap.foldLeft(Map.empty[String, Either[A, Segments[A]]].asRight[DecodingFailure]) {
          case (Right(result), (key, json)) =>
            json
              .as[A]
              .map(_.asLeft[Segments[A]])
              .orElse(json.as[Segments[A]].map(_.asRight))
              .map(value => result + (key -> value))
          case (failure @ Left(_), _) => failure
        }
      }
      .map(Segments[A])
  }

  implicit def encoderSegments[A: Encoder]: Encoder[Segments[A]] = Encoder.instance { values =>
    values.branches.foldLeft(Json.obj()) {
      case (json, (key, Right(segments))) => json deepMerge Json.obj(key := segments)
      case (json, (key, Left(value)))     => json deepMerge Json.obj(key := value)
    }
  }

  implicit val decoderDictionary: Decoder[Dictionary] = Decoder[Segments[Text]].map(Dictionary.apply)

  implicit val encoderDictionary: Encoder[Dictionary] = Encoder[Segments[Text]].contramap(_.values)

  implicit def parserJson[A: Decoder]: Parser[A] = decode[A](_).leftMap(error => Parser.Error(error.show))

  def printerJson[A: Encoder](printer: CircePrinter): Printer[A] = a => printer.print(Encoder[A].apply(a))

  implicit def printerJsonNoSpaces[A: Encoder]: Printer[A] = printerJson[A](CircePrinter.noSpaces)
}
