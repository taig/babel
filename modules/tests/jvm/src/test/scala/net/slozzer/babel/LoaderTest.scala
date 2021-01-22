package net.slozzer.babel

import cats.effect.{IO, Resource}
import munit.CatsEffectSuite

import java.nio.file.{Path => JPath}

abstract class LoaderTest extends CatsEffectSuite {
  def loader: Resource[IO, Loader[IO]]

  test("scan") {
    val root: JPath = JPath
      .of(".")
      .resolve("modules/tests/jvm/target/scala-2.13/test-classes/loader-1/")
      .toAbsolutePath

    loader.use { loader =>
      val obtained = loader.scan("loader-1").map(_.map(root.relativize))
      val returns =
        Set(JPath.of("*.conf"), JPath.of("de.conf"), JPath.of("de-AT.conf"), JPath.of("foo.txt"), JPath.of("bar.svg"))
      assertIO(obtained, returns)
    }
  }
}
