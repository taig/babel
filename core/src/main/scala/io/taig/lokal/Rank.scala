package io.taig.lokal

import cats.Order
import cats.implicits._

sealed abstract class Rank extends Product with Serializable

object Rank {

  /**
    * `Language` and `Country` are equal
    *
    * {{{
    * >>> de_DE"Hallo".translation(Locale.de_DE)
    * Some((Exact, "Hallo"))
    * }}}
    */
  final case object Exact extends Rank

  /**
    * `Language` fallback without `Country`
    *
    * {{{
    * >>> de"Hallo".translation(Locale.de_DE)
    * Some((Language, "Hallo"))
    * }}}
    */
  final case object Language extends Rank

  final case object Universal extends Rank

  implicit val order: Order[Rank] = Order.by {
    case Universal => 1
    case Language  => 2
    case Exact     => 3
  }
}
