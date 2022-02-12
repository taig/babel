# Cookbook

## Resolve the preferred language from the `Accept-Language` header in an http4s middleware

Uses Java's `Locale.LanguageRange.parse` to lift the `Accept-Language` header value into an ordered list of `java.util.Locale`s and aligns it with the `Locale`s that are supported by the application.

```scala mdoc
import java.util.{Locale => JavaLocale}

import scala.jdk.CollectionConverters._

import cats.Monad
import io.taig.babel.Locale
import org.http4s.HttpRoutes
import org.http4s.headers.`Accept-Language`
import org.http4s.implicits._

final class LocalesMiddleware[F[_]: Monad](locales: Set[Locale], fallback: Locale) {
  def apply(routes: Locale => HttpRoutes[F]): HttpRoutes[F] =
    HttpRoutes[F] { request =>
      val locale = request.headers
        .get[`Accept-Language`]
        .map(_.value)
        .flatMap { value =>
          JavaLocale.LanguageRange
            .parse(value)
            .asScala
            .sortWith(_.getWeight > _.getWeight)
            .map(language => JavaLocale.forLanguageTag(language.getRange))
            .flatMap(Locale.fromJavaLocale)
            .find(locales.contains)
        }
        .getOrElse(fallback)

      routes(locale).run(request)
    }
}
```

## Working with ADTs

Add simple helper methods to your data classes to make them easier to use. This works especially well for ADT lookups.

```scala mdoc:to-string
import cats.effect._
import cats.effect.unsafe.implicits.global
import cats.syntax.all._
import io.taig.babel._
import io.taig.babel.generic.auto._

sealed abstract class Country extends Product with Serializable

object Country {
  case object France extends Country
  case object Italy extends Country
}

final case class CountryI18n(france: String, italy: String) {
  def apply(country: Country): String = country match {
    case Country.France => france
    case Country.Italy => italy
  }
}

final case class I18n(country: CountryI18n)

val i18ns = Loader
  .default[IO]
  .load("cookbook-country", Set(Locales.en, Locales.de))
  .map(Decoder[I18n].decodeAll)
  .rethrow
  .map(_.withFallback(Locales.en))
  .flatMap(_.liftTo[IO](new IllegalStateException("Translations for en missing")))
  .unsafeRunSync()
```

```scala mdoc
i18ns(Locales.en).country(Country.Italy)
```

```scala mdoc
i18ns(Locales.de).country(Country.France)
```