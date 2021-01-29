package net.slozzer.babel

import _root_.cats._
import _root_.cats.implicits._

object cats {
  implicit val orderLanguage: Order[Language] = Order.by(_.value)

  implicit val orderCountry: Order[Country] = Order.by(_.value)

  implicit val orderLocale: Order[Locale] = Order.by(locale => (locale.language, locale.country))

  implicit def eqTranslation[A: Eq]: Eq[Translation[A]] = Eq.by(_.toTuple)

  implicit def orderTranslation[A: Order]: Order[Translation[A]] = Order.by(_.toTuple)

  implicit val traverseTranslation: Traverse[Translation] = new Traverse[Translation] {
    override def traverse[G[_]: Applicative, A, B](fa: Translation[A])(f: A => G[B]): G[Translation[B]] =
      f(fa.value).map(fa.as)

    override def foldLeft[A, B](fa: Translation[A], b: B)(f: (B, A) => B): B = f(b, fa.value)

    override def foldRight[A, B](fa: Translation[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = f(fa.value, lb)
  }

  implicit def eqTranslations[A: Order]: Eq[Translations[A]] = Eq.by(_.toList.sorted)

  implicit val traverseTranslations: Traverse[Translations] = new Traverse[Translations] {
    override def map[A, B](fa: Translations[A])(f: A => B): Translations[B] = fa.map(f)

    override def traverse[G[_]: Applicative, A, B](fa: Translations[A])(f: A => G[B]): G[Translations[B]] =
      fa.toMap.toList.traverse { case (locale, a) => f(a).map(Translation(locale, _)) }.map(Translations.from)

    override def foldLeft[A, B](fa: Translations[A], b: B)(f: (B, A) => B): B =
      fa.toMap.toList.foldl(b) { case (b, (_, a)) => f(b, a) }

    override def foldRight[A, B](fa: Translations[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa.toMap.toList.foldr(lb) { case ((_, a), b) => f(a, b) }
  }

  implicit val monoidKTranslations: MonoidK[Translations] = new MonoidK[Translations] {
    override def empty[A]: Translations[A] = Translations.Empty

    override def combineK[A](x: Translations[A], y: Translations[A]): Translations[A] = x concat y
  }

  implicit def eqNonEmptyTranslations[A: Order]: Eq[NonEmptyTranslations[A]] = Eq.by(_.toList.sorted)

  implicit val traverseNonEmptyTranslations: Traverse[NonEmptyTranslations] = new Traverse[NonEmptyTranslations] {
    override def map[A, B](fa: NonEmptyTranslations[A])(f: A => B): NonEmptyTranslations[B] = fa.map(f)

    override def traverse[G[_]: Applicative, A, B](fa: NonEmptyTranslations[A])(
        f: A => G[B]
    ): G[NonEmptyTranslations[B]] =
      (f(fa.default.value).map(fa.default.as), fa.translations.traverse(f)).mapN(NonEmptyTranslations[B])

    override def foldLeft[A, B](fa: NonEmptyTranslations[A], b: B)(f: (B, A) => B): B =
      (fa.default :: fa.translations.toList).map(_.value).foldl(b)(f)

    override def foldRight[A, B](fa: NonEmptyTranslations[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      (fa.default :: fa.translations.toList).map(_.value).foldr(lb)(f)
  }

  implicit val semigroupKNonEmptyTranslations: SemigroupK[NonEmptyTranslations] = new SemigroupK[NonEmptyTranslations] {
    override def combineK[A](x: NonEmptyTranslations[A], y: NonEmptyTranslations[A]): NonEmptyTranslations[A] =
      x concatNet y
  }
}
