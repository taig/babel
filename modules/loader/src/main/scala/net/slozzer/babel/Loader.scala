package net.slozzer.babel

abstract class Loader[F[_]] {
  def load(base: String, locales: Set[Locale]): F[Translations[Babel]]
}