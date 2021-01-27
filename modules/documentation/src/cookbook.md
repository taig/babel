# Cookbook

## Resolve the user's preferred language from the `Accept-Language` header in an http4s middleware

Uses Java's `Locale.LanguageRange.parse` to lift the `Accept-Language` header value into an ordered list of `Locale`s and aligns it with the `Locale`s that are supported by the application.

```scala mdoc
import java.util.{Locale => JavaLocale}

import scala.jdk.CollectionConverters._

import cats.Defer
import net.slozzer.babel.Locale
import org.http4s.HttpRoutes
import org.http4s.headers.`Accept-Language`

final class LocalesMiddleware[F[_]: Defer](locales: Set[Locale], fallback: Locale) {
  def apply(routes: Locale => HttpRoutes[F]): HttpRoutes[F] =
    HttpRoutes[F] { request =>
      val locale = request.headers
        .get(`Accept-Language`)
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