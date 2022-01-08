package net.slozzer.babel.sample.backend

import cats.effect._
import cats.syntax.all._
import net.slozzer.babel._
import net.slozzer.babel.circe._
import net.slozzer.sample.I18n
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.{HttpApp, HttpRoutes, MediaType, StaticFile}

object SampleApp extends IOApp {
  def server[F[_]: Async](app: HttpApp[F]): Resource[F, Server] =
    BlazeServerBuilder[F].bindHttp(host = "0.0.0.0").withHttpApp(app).resource

  val locales = Set(Locales.en)

  def i18n[F[_]: Sync]: F[NonEmptyTranslations[I18n]] =
    Loader
      .default[F]
      .load("i18n", locales)
      .map(Decoder[I18n].decodeAll)
      .rethrow
      .flatMap(_.withFallback(Locales.en).liftTo[F](new IllegalStateException("Translations for en missing")))

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      i18ns <- Resource.eval(i18n[IO])
      middleware = new LocalesMiddleware[IO](locales, fallback = Locales.en)
      app = middleware(SampleRoutes[IO](i18ns, _)).orNotFound
      _ <- server[IO](app)
    } yield ExitCode.Success).use(_ => IO.never)
}

final class SampleRoutes[F[_]: Sync](i18ns: NonEmptyTranslations[I18n]) extends Http4sDsl[F] {
  def routes(locale: Locale): HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      val i18n = i18ns(locale)

      Ok(
        s"""<!DOCTYPE HTML>
           |<html>
           |<head>
           |  <meta charset="utf-8" />
           |  <title>${i18n.index.page.title} | ${i18n.app.name}</title>
           |  ${i18n.index.page.description.map(value => s"""<meta name="description" content="$value"/>""").orEmpty}
           |  <meta name="viewport" content="width=device-width, initial-scale=1" />
           |  <script src="/main.js"></script>
           |</head>
           |<body>
           |  <h1>${i18n.index.headline}</h1>
           |  <p>${i18n.index.introduction}</p>
           |  <p id="message">${i18n.index.message(0)("0")}</p>
           |  <button id="button">${i18n.index.label}</button>
           |</body>
           |</html>""".stripMargin
      ).map(_.withContentType(`Content-Type`(MediaType.text.html)))
    case GET -> Root / "main.js" =>
      StaticFile.fromResource[F]("public/babel-sample-frontend-fastopt.js").getOrElseF(NotFound())
    case GET -> Root / "i18n.json" =>
      Ok(printer.print(Encoder[I18n].encode(i18ns(locale))))
        .map(_.withContentType(`Content-Type`(MediaType.application.json)))
  }
}

object SampleRoutes {
  def apply[F[_]: Sync](i18ns: NonEmptyTranslations[I18n], locale: Locale): HttpRoutes[F] =
    new SampleRoutes[F](i18ns).routes(locale)
}
