package io.taig.babel

import cats.effect.{Blocker, IO}
import io.taig.babel.circe._
import munit.CatsEffectSuite

final class LoaderTest extends CatsEffectSuite {
  test("auto") {
    val obtained = Blocker[IO].use(blocker => Loader.auto[IO](blocker))
    val expected = Babel(
      Segments.one(
        "greeting",
        Translation(
          Map(Locales.de -> Text.one("Guten Tag"), Locales.de_AT -> Text.one("Grüß Gott")),
          Right(Text.one("Hi"))
        )
      )
    )
    assertIO(obtained, expected)
  }
}
