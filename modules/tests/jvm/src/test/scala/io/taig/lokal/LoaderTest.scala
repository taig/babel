package io.taig.lokal

import cats.effect.{Blocker, IO}
import io.taig.lokal.circe._
import munit.CatsEffectSuite

final class LoaderTest extends CatsEffectSuite {
  test("auto") {
    Blocker[IO].use { blocker => Loader.auto[IO](blocker, "i18n", getClass.getClassLoader) }
  }
}
