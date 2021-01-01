package net.slozzer.babel

import org.ekrich.config.{Config, ConfigFactory, ConfigRenderOptions}

import scala.util.control.NonFatal
import io.circe.{Decoder => CirceDecoder}

trait hocon extends circe {
  implicit def parserConfig[A: CirceDecoder]: Parser[A] = { value =>
    try {
      Right[Parser.Error, Config](ConfigFactory.parseString(value)).flatMap { config =>
        circe.parserJson[A].parse(config.root.render(ConfigRenderOptions.concise))
      }
    } catch { case NonFatal(throwable) => Left(Parser.Error("A", Some(throwable))) }
  }
}

object hocon extends hocon
