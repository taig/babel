package io.taig.lokal

package object dsl {
//  implicit final class LokalStringContext(context: StringContext) extends LokalStringContexts {
//    private def substitute(locale: Locale, arguments: Seq[Any]): Seq[String] =
//      arguments.map {
//        case translation: Translation[_] => translation.translate(locale).map(_.toString).getOrElse("???")
//        case dictionary: Dictionary[_]   => dictionary.translate(locale).toString
//        case value                       => value.toString
//      }
//
//    private def merge[A](left: List[A], right: List[A]): List[A] = left match {
//      case head :: tail => head :: merge(right, tail)
//      case Nil          => right
//    }
//
//    protected def apply(locale: Locale, arguments: Seq[Any]): Translation[String] =
//      Translation.one(locale, merge(context.parts.toList, substitute(locale, arguments).toList).mkString)
//
//    def x(arguments: Any*): Dictionary[String] = Dictionary { locale =>
//      Result(Rank.Fallback, merge(context.parts.toList, substitute(locale, arguments).toList).mkString)
//    }
//  }
}
