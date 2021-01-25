package net.slozzer.babel

import cats.data.Chain
import cats.implicits._
import io.circe.Json
import io.circe.{parser => CirceParser}

trait circe {
  private def error(tpe: String, path: Chain[String]): Parser.Error =
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

  def toJson(babel: Babel): Json = babel match {
    case Babel.Object(values) => Json.fromFields(values.fmap(toJson))
    case Babel.Value(value)   => Json.fromString(value)
  }

  val parser: Parser = value =>
    CirceParser
      .parse(value)
      .leftMap(failure => Parser.Error.typeMismatch("Json", cause = Some(failure)))
      .flatMap(toBabel(_, Chain.empty))

  val printer: Printer = toJson(_).spaces2
}

object circe extends circe
