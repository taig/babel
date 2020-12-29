package io.taig.babel

import cats.effect.{Blocker, IO}
import cats.syntax.all._
//import io.taig.babel.circe._
import io.taig.babel.hocon._
import munit.CatsEffectSuite

final class LoaderTest extends CatsEffectSuite {
  test("auto") {
    val obtained = Blocker[IO].use(blocker => Loader.auto[IO](blocker))
    val expected = Babel(
      Segments(
        Map(
          "greeting" -> Left(
            Translation(
              Map(Locales.de -> Text.one("Guten Tag"), Locales.de_AT -> Text.one("Grüß Gott")),
              Right(Text.one("Hi"))
            )
          ),
          "farewell" -> Left(
            Translation(
              Map(Locales.de -> Text.one("Auf Wiedersehen"), Locales.de_AT -> Text.one("Grüß Gott")),
              Left("[farewell]")
            )
          )
        )
      )
    )

    assertIO(obtained, expected)
  }

  test("missing locales") {
    val obtained = Blocker[IO]
      .use { blocker => Loader.auto[IO](blocker).flatTap(Loader.verifyMissingLocales[IO](_, Set(Locales.fr))) }
      .attempt
      .map(_.leftMap(_.getMessage))

    val expected =
      """Missing translations
        |fr:
        | - farewell""".stripMargin

    assertIO(obtained, Left(expected))
  }
}
