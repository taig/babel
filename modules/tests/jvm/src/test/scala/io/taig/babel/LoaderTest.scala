package io.taig.babel

import _root_.cats.effect.{IO, Resource}
import munit.CatsEffectSuite

abstract class LoaderTest extends CatsEffectSuite {
  def loader: Resource[IO, Loader[IO]]

  test("load") {
    loader
      .use(_.load("loader-1", Set(Locales.en, Locales.de)))
      .map { obtained =>
        val expected = Translations(Map(Locales.en -> Babel.Empty, Locales.de -> Babel.Empty))
        assertEquals(obtained, expected)
      }
  }

  test("load with HOCON substitutions resolved") {
    loader
      .use(_.load("loader-substitutions", Set(Locales.en, Locales.de)))
      .map { obtained =>
        val expected = Translations(
          Map(
            Locales.en -> Babel.Object(
              Map(
                "app" -> Babel.Object(Map("name" -> Babel.Value("App name"))),
                "label" -> Babel.Object(Map("title" -> Babel.Value("App name")))
              )
            ),
            Locales.de -> Babel.Object(
              Map(
                "app" -> Babel.Object(Map("name" -> Babel.Value("Applikationsname"))),
                "label" -> Babel.Object(Map("title" -> Babel.Value("Applikationsname")))
              )
            )
          )
        )
        assertEquals(obtained, expected)
      }
  }
}
