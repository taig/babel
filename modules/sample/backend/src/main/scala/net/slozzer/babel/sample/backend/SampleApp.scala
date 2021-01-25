package net.slozzer.babel.sample.backend

import scala.concurrent.ExecutionContext

import cats.effect.{Blocker, Concurrent, ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Resource, Timer}
import cats.syntax.all._
import cats.{Applicative, Defer}
import net.slozzer.babel._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{HttpApp, HttpRoutes, MediaType}

object SampleApp extends IOApp {
  def server[F[_]: ConcurrentEffect: Timer](context: ExecutionContext, app: HttpApp[F]): Resource[F, Server[F]] =
    BlazeServerBuilder[F](context).bindHttp(host = "0.0.0.0").withHttpApp(app).resource

  val locales = Set(Locales.de, Locales.en)

  def i18n[F[_]: Concurrent: ContextShift](blocker: Blocker): F[Dictionary[I18n]] =
    HoconLoader[F](blocker)
      .load("i18n", locales)
      .map(Decoder[I18n].decodeAll)
      .rethrow
      .flatMap { translations =>
        translations
          .get(Locales.en)
          .liftTo[F](new IllegalStateException("Translations for en missing"))
          .map(fallback => (translations - Locales.en).toDictionary(fallback))
      }

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      blocker <- Blocker[IO]
      i18ns <- Resource.liftF(i18n[IO](blocker))
      middleware = new LocalesMiddleware[IO](locales, fallback = Locales.en)
      app = middleware(SampleRoutes[IO](i18ns, _)).orNotFound
      _ <- server[IO](ExecutionContext.global, app)
    } yield ExitCode.Success).use(_ => IO.never)
}

final class SampleRoutes[F[_]: Applicative: Defer](i18ns: Dictionary[I18n]) extends Http4sDsl[F] {
  def routes(locale: Locale): HttpRoutes[F] = HttpRoutes.of[F] { case GET -> Root =>
    val i18n = i18ns(locale)

    Ok(
      s"""<!DOCTYPE HTML>
         |<html>
         |<head>
         |  <meta charset="utf-8" />
         |  <title>${i18n.index.title} | ${i18n.app.name}</title>
         |  <meta name="viewport" content="width=device-width, initial-scale=1" />
         |  <script charset="utf-8" src="js/app.js"></script>
         |  <link rel="stylesheet" href="css/app.css" />
         |</head>
         |<body>
         |  <h1>${i18n.index.headline}</h1>
         |  <p>${i18n.index.message(0)}</p>
         |  <button>${i18n.index.label}</button>
         |</body>
         |</html>""".stripMargin
    ).map(_.withContentType(`Content-Type`(MediaType.text.html)))
  }
}

object SampleRoutes {
  def apply[F[_]: Applicative: Defer](i18ns: Dictionary[I18n], locale: Locale): HttpRoutes[F] =
    new SampleRoutes[F](i18ns).routes(locale)
}
