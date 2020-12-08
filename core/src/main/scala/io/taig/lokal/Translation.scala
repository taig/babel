package io.taig.lokal

import cats.Invariant
import cats.syntax.all._
import io.taig.lokal.util.{ArgumentEncoder, QuantityEncoder}

sealed abstract class Translation[-A, B] extends Product with Serializable {
  def apply(locale: Locale, arguments: A): Option[Result[B]]

  final def translate(locale: Locale, arguments: A): B =
    apply(locale, arguments).map(_.value).getOrElse(enforce(locale, arguments))

  /** Get the first translation that is available, mostly ignoring the given `Locale` */
  def enforce(locale: Locale, arguments: A): B

  final def ~[A1 <: A](translation: Translation[A1, B]): Translation[A1, B] =
    Translation.Or(this, translation)

  def imap[X](f: B => X)(g: X => B): Translation[A, X]
}

object Translation {
  final case class Arguments[A, B, C](encoder: ArgumentEncoder[A, C], translation: Translation[B, C])
      extends Translation[(A, B), C] {
    override def apply(locale: Locale, arguments: (A, B)): Option[Result[C]] =
      translation(locale, arguments._2).map { result =>
        result.copy(value = encoder.apply(locale, arguments._1, result.value))
      }

    override def enforce(locale: Locale, arguments: (A, B)): C =
      // TODO we probably want to pass the locale that ended up being used in `enforce` also to `encoder`
      encoder(locale, arguments._1, translation.enforce(locale, arguments._2))

    override def imap[D](f: C => D)(g: D => C): Translation[(A, B), D] = ???
  }

  final case class Or[A, B](left: Translation[A, B], right: Translation[A, B]) extends Translation[A, B] {
    override def apply(locale: Locale, arguments: A): Option[Result[B]] =
      List(left(locale, arguments), right(locale, arguments)).flatten.maximumByOption(_.rank)

    override def enforce(locale: Locale, arguments: A): B = left.enforce(locale, arguments)

    override def imap[C](f: B => C)(g: C => B): Translation[A, C] = ???
  }

  final case class Specific[A, B](locale: Locale, translation: Translation[A, B]) extends Translation[A, B] {
    override def apply(needle: Locale, arguments: A): Option[Result[B]] =
      if (needle === locale) translation(needle, arguments).map(_.copy(rank = Rank.Exact))
      else if (needle.withoutCountry === locale) translation(needle, arguments).map(_.copy(rank = Rank.Language))
      else None

    override def enforce(locale: Locale, arguments: A): B = translation.enforce(locale, arguments)

    override def imap[C](f: B => C)(g: C => B): Translation[A, C] = ???
  }

  final case class Universal[A](value: A) extends Translation[Any, A] {
    override def apply(locale: Locale, arguments: Any): Option[Result[A]] = Some(Result(Rank.Fallback, value))

    override def enforce(locale: Locale, arguments: Any): A = value

    override def imap[C](f: A => C)(g: C => A): Translation[Any, C] = ???
  }

  final case class Plurals[A, B](
      encoder: QuantityEncoder[B],
      identifier: String,
      default: Translation[(A, Quantity), B],
      quantities: Map[Quantity, Translation[(A, Quantity), B]]
  ) extends Translation[(A, Quantity), B] {
    override def apply(locale: Locale, arguments: (A, Quantity)): Option[Result[B]] =
      (quantities.get(arguments._2) match {
        case Some(translation) => translation(locale, arguments)
        case None              => default(locale, arguments)
      }).map(_.map(encoder.apply(arguments._2, identifier, _)))

    override def enforce(locale: Locale, arguments: (A, Quantity)): B =
      quantities.get(arguments._2).map(_.enforce(locale, arguments)).getOrElse(default.enforce(locale, arguments))

    override def imap[C](f: B => C)(g: C => B): Translation[(A, Quantity), C] =
      Plurals(encoder.imap(f)(g), identifier, default.imap(f)(g), quantities.fmap(_.imap(f)(g)))
  }

//  final case class Provide[A, B, C](
//      value: B,
//      translation: Translation[(A, B), C]
//  ) extends Translation[A, C] {
//    override def apply(locale: Locale, arguments: A): Option[Result[C]] =
//      translation(locale, (arguments, value))
//
//    override def enforce(locale: Locale, arguments: A): C = ???
//
//    override def imap[X](f: C => X)(g: X => C): Translation[A, X] = ???
//  }

  def apply[A, B](locale: Locale, translation: Translation[A, B]): Specific[A, B] = Specific(locale, translation)

  def universal[A](value: A): Translation[Any, A] = Universal(value)

  def arguments[A, B, C](
      translation: Translation[B, C]
  )(implicit encoder: ArgumentEncoder[A, C]): Translation[(A, B), C] =
    Arguments(encoder, translation)

  implicit def invariant[A]: Invariant[Translation[A, *]] = new Invariant[Translation[A, *]] {
    override def imap[B, C](fa: Translation[A, B])(f: B => C)(g: C => B): Translation[A, C] = fa.imap(f)(g)
  }
}
