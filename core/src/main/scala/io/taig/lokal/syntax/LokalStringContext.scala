package io.taig.lokal.syntax

import java.util.Locale

import io.taig.lokal._
import cats.implicits._
import io.taig.lokal.LokalStringOperations

final class LokalStringContext(context: StringContext)
    extends LokalStringOperations {
  private def substitute(arguments: Seq[Any]): Seq[Translation[String]] =
    arguments.map {
      case translation: Translation[_] ⇒ translation.map(_.toString)
      case value ⇒ Translation(Locales.en, value.toString)
    }

  private def merge[A](left: List[A], right: List[A]): List[A] = left match {
    case head :: tail => head :: merge(right, tail)
    case Nil          => right
  }

  protected def apply(locale: Locale,
                      arguments: Seq[Any]): Translation[String] = {
    merge(context.parts.map(Translation(locale, _)).toList,
          substitute(arguments).toList).reduce(_ |+| _)
  }
}
