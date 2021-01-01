package net.slozzer.babel

import hocon._

final class HoconLoaderTest extends LoaderTest {
  implicit override val parser: Parser[Dictionary] = parserConfig[Dictionary]

  override val extension: String = "conf"
}
