package io.taig.lokal

import cats.data.NonEmptyList

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