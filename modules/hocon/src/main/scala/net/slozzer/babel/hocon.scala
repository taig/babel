package net.slozzer.babel

import org.ekrich.config.{Config, ConfigFactory}

import scala.util.control.NonFatal

trait hocon {
  implicit val parserConfig: Parser[Config] = Parser[String].emap { raw =>
    try Right(ConfigFactory.parseString(raw)) catch {
      case NonFatal(throwable) => Left(Parser.Error.typeMismatch("Config", Some(throwable)))
    }
  }
}

object hocon extends hocon
