package io.taig.lokal

import java.util.Locale

import scala.annotation.tailrec

abstract class Translation[A] extends (Locale => A) {
  override final def apply(locale: Locale): A =
    Translation.translate(locale, this, this)

  def &(translation: Translation[Option[A]]): Translation[A] =
    Translation(locale => translation(locale).getOrElse(apply(locale)))
}

object Translation {
  final case class Apply[A](translate: Locale => A) extends Translation[A]

  final case class Specific[A](locale: Locale,
                               value: A,
                               continue: Translation[A])
      extends Translation[A]

  final case class Wildcard[A](value: A) extends Translation[A]

  @tailrec
  private def translate[A](locale: Locale,
                           translation: Translation[A],
                           root: Translation[A]): A =
    translation match {
      case Apply(translate)                             => translate(locale)
      case Specific(`locale`, value, _)                 => value
      case Specific(_, _, continue)                     => translate(locale, continue, root)
      case Wildcard(value) if locale.getCountry() == "" => value
      case Wildcard(_) =>
        translate(new Locale(locale.getLanguage()), root, root)
    }

  def apply[A](translate: Locale => A): Translation[A] = Apply(translate)

  def apply[A](locale: Locale, value: A): Translation[Option[A]] =
    Specific(locale, Some(value), lift(None))

  def lift[A](value: A): Translation[A] = Wildcard(value)
}
