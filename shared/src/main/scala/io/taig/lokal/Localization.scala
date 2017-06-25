package io.taig.lokal

import cats.data.NonEmptyList

case class Localization[A]( identifier: Identifier, value: A ) {
    def &( localization: Localization[A] ): Translation[A] =
        Translation( NonEmptyList.of( this, localization ) )

    override def toString: String = s"""$identifier"$value""""
}