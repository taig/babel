package net.slozzer.babel.sample.backend

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
