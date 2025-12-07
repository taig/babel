package io.taig.babel.sample.frontend

import cats.effect._
import cats.syntax.all._
import io.taig.babel.Decoder
import io.taig.babel.circe.parser
import io.taig.sample.I18n
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.document

import scala.concurrent.Future
import scala.concurrent.Promise

object FrontendApp extends IOApp.Simple {
  override def run: IO[Unit] = for {
    i18n <- request[IO]
    _ <- listen[IO](i18n)
  } yield ()

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

  private def ajaxGet(url: String, timeout: Int = 5000): Future[dom.XMLHttpRequest] = {
    val req = new dom.XMLHttpRequest()
    val promise = Promise[dom.XMLHttpRequest]()

    req.onreadystatechange = { _ =>
      if (req.readyState == 4) {
        if ((req.status >= 200 && req.status < 300) || req.status == 304)
          promise.success(req)
        else
          promise.failure(new IllegalStateException(s"Unexpected response status: ${req.status} ${req.statusText}"))
      }
    }
    req.open("GET", url)
    req.responseType = ""
    req.timeout = timeout.toDouble
    req.send()
    promise.future
  }

  def request[F[_]](implicit F: Async[F]): F[I18n] = F
    .fromFuture(F.delay(ajaxGet("http://localhost:8080/i18n.json")))
    .map(_.responseText)
    .map(parser.parse)
    .rethrow
    .map(Decoder[I18n].decode)
    .rethrow
}
