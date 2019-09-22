package io.taig.lokal.syntax

trait StringSyntax {
  implicit def lokalStringContext(context: StringContext): LokalStringContext =
    new LokalStringContext(context)
}
