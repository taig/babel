package io.taig.babel.sample.backend

import cats.Monad
import io.taig.babel.Locale
import org.http4s.HttpRoutes
import org.http4s.headers.`Accept-Language`
import org.http4s.implicits._

import java.util.{Locale => JavaLocale}
import scala.jdk.CollectionConverters._

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
