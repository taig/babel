package io.taig.lokal

import cats.Show
import cats.Eq
import cats.implicits._
import cats.data.NonEmptyList
import cats.Semigroup

case class Translation[A]( values: NonEmptyList[Localization[A]] )
        extends AnyVal {
    def translate( identifier: Identifier ): A =
        find( identifier, Identifier.Comparison.Exact )
            .orElse( find( identifier, Identifier.Comparison.Almost ) )
            .orElse( find( identifier, Identifier.Comparison.Weak ) )
            .getOrElse( values.head )
            .value

    private def find(
        identifier: Identifier,
        comparison: Identifier.Comparison
    ): Option[Localization[A]] = values.find { localization ⇒
        ( identifier compare localization.identifier ) == comparison
    }

    def &( localization: Localization[A] ): Translation[A] =
        Translation( NonEmptyList( values.head, values.tail :+ localization ) )

    override def toString: String = values.toList.mkString( " & " )
}

object Translation {
    implicit def eq[A: Eq]: Eq[Translation[A]] = Eq.by( _.values )

    implicit def show[A]: Show[Translation[A]] = Show.fromToString

    implicit def semigroup[A]: Semigroup[Translation[A]] =
        new Semigroup[Translation[A]] {
            override def combine(
                a: Translation[A],
                b: Translation[A]
            ): Translation[A] =
                Translation( a.values combine b.values )
        }
}