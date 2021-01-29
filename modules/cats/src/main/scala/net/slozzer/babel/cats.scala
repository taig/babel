package net.slozzer.babel

import _root_.cats._
import _root_.cats.implicits._

object cats {
  implicit val orderLanguage: Order[Language] = Order.by(_.value)

  implicit val orderCountry: Order[Country] = Order.by(_.value)

  implicit val orderLocale: Order[Locale] = Order.by(locale => (locale.language, locale.country))

  implicit def eqTranslations[A: Eq]: Eq[Translations[A]] = Eq.by(_.values.toList.sortBy(_._1))

  implicit val traverseTranslations: Traverse[Translations] = new Traverse[Translations] {
    override def map[A, B](fa: Translations[A])(f: A => B): Translations[B] = fa.map(f)

    override def traverse[G[_]: Applicative, A, B](fa: Translations[A])(f: A => G[B]): G[Translations[B]] =
      fa.toMap.toList.traverse { case (locale, a) => f(a).tupleLeft(locale) }.map(Translations.from)

    override def foldLeft[A, B](fa: Translations[A], b: B)(f: (B, A) => B): B =
      fa.toMap.toList.foldl(b) { case (b, (_, a)) => f(b, a) }

    override def foldRight[A, B](fa: Translations[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa.toMap.toList.foldr(lb) { case ((_, a), b) => f(a, b) }
  }

  implicit def eqDictionary[A: Eq]: Eq[NonEmptyTranslations[A]] = Eq.by(_.toMap.toList.sortBy(_._1))

  implicit val traverseDictionary: Traverse[NonEmptyTranslations] = new Traverse[NonEmptyTranslations] {
    override def map[A, B](fa: NonEmptyTranslations[A])(f: A => B): NonEmptyTranslations[B] = fa.map(f)

    override def traverse[G[_]: Applicative, A, B](fa: NonEmptyTranslations[A])(
        f: A => G[B]
    ): G[NonEmptyTranslations[B]] =
      (fa.translations.traverse(f), f(fa.fallback._2).tupleLeft(fa.fallback._1)).mapN(NonEmptyTranslations[B])

    override def foldLeft[A, B](fa: NonEmptyTranslations[A], b: B)(f: (B, A) => B): B =
      (fa.fallback :: fa.translations.toMap.toList).map(_._2).foldl(b)(f)

    override def foldRight[A, B](fa: NonEmptyTranslations[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      (fa.fallback :: fa.translations.toMap.toList).map(_._2).foldr(lb)(f)
  }
}
