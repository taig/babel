package io.taig.lokal

import org.scalacheck.Cogen

object Cogens {
  val rank: Cogen[Rank] = implicitly[Cogen[Int]].contramap {
    case Rank.Exact     => 4
    case Rank.Language  => 3
    case Rank.Universal => 2
    case Rank.Country   => 1
  }

  val language: Cogen[Language] = implicitly[Cogen[String]].contramap(_.value)

  val country: Cogen[Country] = implicitly[Cogen[String]].contramap(_.value)

  val locale: Cogen[Locale] = {
    Cogen(f = { (seed, locale) =>
      Cogen
        .cogenOption(country)
        .perturb(language.perturb(seed, locale.language), locale.country)
    })
  }
}
