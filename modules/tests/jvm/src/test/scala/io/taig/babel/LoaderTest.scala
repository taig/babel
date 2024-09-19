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
}
