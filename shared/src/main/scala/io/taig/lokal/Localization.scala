package io.taig.lokal

import cats.Show
import cats.data.NonEmptyList

case class Localization[A]( identifier: Identifier, value: A ) {
    def &( localization: Localization[A] ): Translation[A] =
        Translation( NonEmptyList.of( this, localization ) )

    override def toString: String = s"""$identifier"$value""""
}

object Localization {
    implicit def show[A]: Show[Localization[A]] = Show.fromToString
}