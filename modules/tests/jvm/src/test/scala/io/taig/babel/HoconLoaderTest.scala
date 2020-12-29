package io.taig.babel

import io.taig.babel.hocon._

final class HoconLoaderTest extends LoaderTest {
  implicit override val parser: Parser[Dictionary] = parserConfig[Dictionary]

  override val extension: String = "conf"
}
