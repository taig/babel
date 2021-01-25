package net.slozzer.babel

import java.nio.file.{Path => JPath}

import cats.effect.{IO, Resource}
import munit.CatsEffectSuite

abstract class LoaderTest extends CatsEffectSuite {
  def loader: Resource[IO, Loader[IO]]

  test("scan") {
    val root: JPath = JPath
      .of(".")
      .resolve("modules/tests/jvm/target/scala-2.13/test-classes/loader-1/")
      .toAbsolutePath

    loader.use { loader =>
      val obtained = loader.scan("loader-1").map(_.map(root.relativize))
      val returns = Set("*.conf", "de.conf", "en.conf", "foo.txt", "bar.svg").map(JPath.of(_))
      assertIO(obtained, returns)
    }
  }

  test("filter") {
    val paths = Set("i18n/en.conf", "i18n/de.conf", "i18n/fr.json", "babel/es.conf").map(JPath.of(_))
    val obtained = Loader.filter(paths, PathFilter.extension("conf") && PathFilter.parent("i18n"))
    val expected = Translations.of(
      Locales.en -> JPath.of("i18n/en.conf"),
      Locales.de -> JPath.of("i18n/de.conf")
    )
    assertEquals(obtained, expected)
  }
}
