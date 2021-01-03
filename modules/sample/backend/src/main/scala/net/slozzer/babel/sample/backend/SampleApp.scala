package net.slozzer.babel.sample.backend

import scala.concurrent.ExecutionContext

import cats.{Applicative, Defer}
import cats.effect.{Blocker, ConcurrentEffect, ExitCode, IO, IOApp, Resource, Timer}
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._

object SampleApp extends IOApp {
  def server[F[_]: ConcurrentEffect: Timer](context: ExecutionContext, app: HttpApp[F]): Resource[F, Server[F]] =
    BlazeServerBuilder[F](context).bindHttp(host = "0.0.0.0").withHttpApp(app).resource

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      blocker <- Blocker[IO]
      app = SampleRoutes[IO].orNotFound
      _ <- server[IO](ExecutionContext.global, app)
    } yield ExitCode.Success).use(_ => IO.never)
}

final class SampleRoutes[F[_]: Applicative: Defer] extends Http4sDsl[F] {
  def routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root => Ok("cool")
  }
}

object SampleRoutes {
  def apply[F[_]: Applicative: Defer]: HttpRoutes[F] = new SampleRoutes[F].routes
}
