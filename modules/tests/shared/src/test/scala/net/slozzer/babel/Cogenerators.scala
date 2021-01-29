package net.slozzer.babel

import _root_.cats.implicits._
import net.slozzer.babel.cats._
import org.scalacheck.Cogen

object Cogenerators {
  implicit val language: Cogen[Language] = Cogen[String].contramap(_.value)

  implicit val country: Cogen[Country] = Cogen[String].contramap(_.value)

  implicit val locale: Cogen[Locale] = Cogen[(Language, Option[Country])].contramap { locale =>
    (locale.language, locale.country)
  }

  implicit val quantity: Cogen[Quantity] = Cogen[Either[Int, (Int, Int)]].contramap {
    case Quantity.Exact(value)      => Left(value)
    case Quantity.Range(start, end) => Right((start, end))
  }

  implicit def quantitiesElement[A: Cogen]: Cogen[Quantities.Element[A]] =
    Cogen[(Quantity, A)].contramap(element => (element.quantity, element.value))

  implicit def quantities[A: Cogen]: Cogen[Quantities[A]] =
    Cogen[(A, List[Quantities.Element[A]])].contramap(quantities => (quantities.default, quantities.quantities))

  implicit def translation[A: Cogen]: Cogen[Translation[A]] = Cogen[(Locale, A)].contramap(_.toTuple)

  implicit def translations[A: Cogen]: Cogen[Translations[A]] =
    Cogen.cogenMap[Locale, A].contramap(_.values)

  implicit def nonEmptyTranslations[A: Cogen]: Cogen[NonEmptyTranslations[A]] =
    Cogen[(Translation[A], Translations[A])].contramap(translations =>
      (translations.default, translations.translations)
    )
}
