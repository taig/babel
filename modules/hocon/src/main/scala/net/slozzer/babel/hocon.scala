package net.slozzer.babel

import org.ekrich.config.{Config, ConfigFactory, ConfigRenderOptions}

import scala.util.control.NonFatal

trait hocon {
  val parser: Parser = new Parser {
    override def parse(value: String): Either[Parser.Error, Babel] = {
      try {
        Right(ConfigFactory.parseString(value)).flatMap { config =>
          circe.parser.parse(config.root.render(ConfigRenderOptions.concise))
        }
      } catch {
        case NonFatal(throwable) => Left(Parser.Error.typeMismatch("Config", Some(throwable)))
      }
    }
  }
}

object hocon extends hocon
