package net.slozzer.babel.sample.backend

import java.nio.charset.StandardCharsets

import cats.effect.{Blocker, Concurrent, ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Resource, Timer}
import cats.syntax.all._
import net.slozzer.babel.hocon._
import net.slozzer.babel.{Babel, ClassgraphLoader, PathFilter, Translations}
import org.http4s.HttpApp
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

object SampleApp extends IOApp {
  def server[F[_]: ConcurrentEffect: Timer](context: ExecutionContext, app: HttpApp[F]): Resource[F, Server[F]] =
    BlazeServerBuilder[F](context).bindHttp(host = "0.0.0.0").withHttpApp(app).resource

  def i18n[F[_]: Concurrent: ContextShift](blocker: Blocker): F[Translations[Babel]] =
    ClassgraphLoader[F](blocker).use { loader =>
      loader
        .scan("i18n")
        .map(loader.filter(_, PathFilter.extension("conf")))
        .flatMap(loader.load)
        .map(_.map(new String(_, StandardCharsets.UTF_8)))
        .map(parser.parseAll)
        .rethrow
    }

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      blocker <- Blocker[IO]
      i18ns <- Resource.liftF(i18n[IO](blocker))
      _ = println(i18ns)
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
