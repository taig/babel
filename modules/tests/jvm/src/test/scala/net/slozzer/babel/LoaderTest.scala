package net.slozzer.babel

import cats.effect.{IO, Resource}
import munit.CatsEffectSuite

import java.nio.file.{Path => JPath}

abstract class LoaderTest extends CatsEffectSuite {
  def loader: Resource[IO, Loader[IO]]

  val Root: JPath = JPath
    .of(".")
    .resolve("modules/tests/jvm/target/scala-2.13/test-classes/")
    .toAbsolutePath

  test("scan") {
    loader.use { loader =>
      val run = loader.scan("loader-1.conf").map(_.map(Root.relativize))
      assertIO(
        obtained = run,
        returns = Set(JPath.of("loader-1/*.conf"), JPath.of("loader-1/de.conf"), JPath.of("loader-1/de-AT.conf"))
      )
    }
  }
}
