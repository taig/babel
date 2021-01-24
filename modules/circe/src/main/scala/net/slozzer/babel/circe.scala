package net.slozzer.babel

import cats.data.Chain
import cats.implicits._
import io.circe.parser._
import io.circe.{Json, Printer => CircePrinter}

trait circe {
  val parser: Parser = new Parser {
    override def parse(value: String): Either[Parser.Error, Babel] =
      io.circe.parser
        .parse(value)
        .leftMap(failure => Parser.Error.typeMismatch("Json", cause = Some(failure)))
        .flatMap(toBabel(_, Chain.empty))

    def error(tpe: String, path: Chain[String]): Parser.Error =
      Parser.Error(s"Unsupported JSON type: $tpe (${path.mkString_(".", ".", "")})", cause = None)

    def toBabel(json: Json, path: Chain[String]): Either[Parser.Error, Babel] =
      json.fold(
        Left(error("NULL", path)),
        _ => Left(error("BOOLEAN", path)),
        _ => Left(error("NUMBER", path)),
        value => Right(Babel.Value(value)),
        _ => Left(error("ARRAY", path)),
        json =>
          json.toMap.toList
            .traverse { case (key, json) =>
              toBabel(json, path :+ key).tupleLeft(key)
            }
            .map(values => Babel.Object(values.toMap))
      )
  }
}

object circe extends circe
