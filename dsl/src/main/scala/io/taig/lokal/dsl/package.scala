package io.taig.lokal

import cats.implicits._

package object dsl {
  implicit final class LokalStringContext(context: StringContext) extends LokalStringContexts {
    private def substitute(arguments: Seq[Any]): Seq[Translation[String]] =
      arguments.map {
        case translation: Translation[_] => translation.map(_.toString)
        case value                       => Translation.universal(value.toString)
      }

    private def merge[A](left: List[A], right: List[A]): List[A] = left match {
      case head :: tail => head :: merge(right, tail)
      case Nil          => right
    }

    protected def apply(
        locale: Locale,
        arguments: Seq[Any]
    ): Translation[String] = {
      merge(
        context.parts.map(Translation(locale, _)).toList,
        substitute(arguments).toList
      ).reduce((_, _).mapN(_ ++ _))
    }
  }
}
