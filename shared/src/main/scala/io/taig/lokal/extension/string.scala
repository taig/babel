package io.taig.lokal.extension

import io.taig.lokal.operation

import scala.language.implicitConversions

trait string {
  implicit def lokalStringContext(
      context: StringContext
  ): operation.string = new operation.string(context)
}

object string extends string
