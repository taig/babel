package io.taig.babel

final case class Translation[+A](locale: Locale, value: A) {
  def map[B](f: A => B): Translation[B] = copy(value = f(value))

  def as[B](value: B): Translation[B] = map(_ => value)

  def mapWithLocale[B](f: (Locale, A) => B): Translation[B] = copy(value = f(locale, value))

  def toTuple: (Locale, A) = (locale, value)

  override def toString: String = s"$locale -> $value"
}
