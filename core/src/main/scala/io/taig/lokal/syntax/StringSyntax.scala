package io.taig.lokal.syntax

import scala.language.implicitConversions

trait StringSyntax {
  implicit def lokalStringContext(context: StringContext): LokalStringContext =
    new LokalStringContext(context)
}
