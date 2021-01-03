package net.slozzer.babel.sample.backend

final case class I18n[A](app: I18n.App[A], index: I18n.Index[A])

object I18n {
  final case class App[A](name: A)

  final case class Index[A](title: A, headline: A, message: A, label: A)
}
