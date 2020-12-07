package io.taig.lokal

import cats.syntax.all._
import io.circe.Printer
import io.circe.parser.parse
import io.taig.lokal.util.{Decoder, Encoder}

object circe {
  def encoder[A: JsonEncoder](printer: Printer): Encoder[A] = input => printer.print(JsonEncoder[A].apply(input))

  implicit def defaultEncoder[A: JsonEncoder]: Encoder[A] = encoder(Printer.noSpaces)

  implicit def decoder[A: JsonDecoder]: Decoder[A] =
    (mode, strict, input) =>
      parse(input)
        .leftMap(failure => Decoder.Error("Failed to decode JSON", Some(failure)))
        .flatMap(json => JsonDecoder[A].apply(mode, strict, json))
}
