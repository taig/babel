package net.slozzer.babel.sample.frontend

import cats.effect.{Async, ExitCode, IO, IOApp, Sync}
import cats.syntax.all._
import net.slozzer.babel.Decoder
import net.slozzer.babel.circe.parser
import net.slozzer.sample.I18n
import org.scalajs.dom.{document, Event}
import org.scalajs.dom.ext.Ajax

object FrontendApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      i18n <- request[IO]
      _ <- listen[IO](i18n)
    } yield ExitCode.Success

  def listen[F[_]](i18n: I18n)(implicit F: Sync[F]): F[Unit] = F.delay {
    val message = document.getElementById("message")
    val button = document.getElementById("button")
    var counter = 0

    button.addEventListener(
      "click",
      { (_: Event) =>
        counter += 1
        message.innerText = i18n.index.message(counter)(String.valueOf(counter))
      }
    )
  }

  def request[F[_]: ContextShift](implicit F: Async[F]): F[I18n] =
    Async
      .fromFuture(F.delay(Ajax.get("http://localhost:8080/i18n.json")))
      .map(_.responseText)
      .map(parser.parse)
      .rethrow
      .map(Decoder[I18n].decode)
      .rethrow
}
