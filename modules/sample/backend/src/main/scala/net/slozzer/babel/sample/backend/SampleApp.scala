package net.slozzer.babel.sample.backend

import scala.concurrent.ExecutionContext

import cats.syntax.all._
import cats.effect.{Blocker, ConcurrentEffect, ExitCode, IO, IOApp, Resource, Timer}
import cats.{Applicative, Defer}
import net.slozzer.babel.{Decoder, Loader, Locale, Locales, Translation}
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{HttpApp, HttpRoutes, MediaType}
import org.http4s.headers.`Content-Type`

object SampleApp extends IOApp {
  def server[F[_]: ConcurrentEffect: Timer](context: ExecutionContext, app: HttpApp[F]): Resource[F, Server[F]] =
    BlazeServerBuilder[F](context).bindHttp(host = "0.0.0.0").withHttpApp(app).resource

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      blocker <- Blocker[IO]
      result <- Resource.liftF(Loader.scan[IO](blocker, "i18n.conf"))
      _ = println(result)
//      babel <- Resource.liftF(Loader.auto[IO](blocker)).evalTap(Loader.verifyAllMissingLocales[IO])
//      i18ns <- Resource.liftF(IO.fromEither(Decoder[I18n, Translation].decode(babel.values)))
//      middleware = new LocalesMiddleware[IO](babel.locales, Locales.en)
//      app = middleware(SampleRoutes[IO](i18ns, _)).orNotFound
//      _ <- server[IO](ExecutionContext.global, app)
    } yield ExitCode.Success).use(_ => IO.never)
}

//final class SampleRoutes[F[_]: Applicative: Defer](i18ns: I18n[Translation]) extends Http4sDsl[F] {
//  def routes(locale: Locale): HttpRoutes[F] = HttpRoutes.of[F] { case GET -> Root =>
//    Ok(
//      s"""<!DOCTYPE HTML>
//         |<html>
//         |<head>
//         |  <meta charset="utf-8" />
//         |  <title>${i18ns.index.title(locale)} | ${i18ns.app.name(locale)}</title>
//         |  <meta name="viewport" content="width=device-width, initial-scale=1" />
//         |  <script charset="utf-8" src="js/app.js"></script>
//         |  <link rel="stylesheet" href="css/app.css" />
//         |</head>
//         |<body>
//         |  <h1>${i18ns.index.headline(locale)}</h1>
//         |  <p>${i18ns.index.message(locale, Seq(0))}</p>
//         |  <button>${i18ns.index.label(locale)}</button>
//         |</body>
//         |</html>""".stripMargin
//    ).map(_.withContentType(`Content-Type`(MediaType.text.html)))
//  }
//}
//
//object SampleRoutes {
//  def apply[F[_]: Applicative: Defer](i18ns: I18n[Translation], locale: Locale): HttpRoutes[F] =
//    new SampleRoutes[F](i18ns).routes(locale)
//}
