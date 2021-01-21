package net.slozzer.babel

import cats.effect.{Blocker, IO}
import cats.syntax.all._
import net.slozzer.babel.Locales
import munit.CatsEffectSuite

abstract class LoaderTest extends CatsEffectSuite {
  implicit def parser: Parser[Dictionary]

  def extension: String

  test("auto") {
    val obtained = Blocker[IO].use(blocker => Loader.auto[IO](blocker, extension = extension))
    val expected = Babel(
      Segments(
        Map(
          "greeting" -> Left(
            Translation(
              Map(Locales.de -> Quantities.one("Guten Tag"), Locales.de_AT -> Quantities.one("Grüß Gott")),
              Right(Quantities.one("Hi"))
            )
          ),
          "farewell" -> Left(
            Translation(
              Map(Locales.de -> Quantities.one("Auf Wiedersehen"), Locales.de_AT -> Quantities.one("Grüß Gott")),
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
      .use { blocker =>
        Loader.auto[IO](blocker, extension = extension).flatTap(Loader.verifyMissingLocales[IO](_, Set(Locales.fr)))
      }
      .attempt
      .map(_.leftMap(_.getMessage))

    val expected =
      """Missing translations
        |fr:
        | - farewell""".stripMargin

    assertIO(obtained, Left(expected))
  }
}
