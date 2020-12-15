package io.taig.lokal

object dsl {
  implicit final class TextOps(val text: Text) extends AnyVal {
    def apply(quantity: Quantity, arguments: Seq[Any]): String = String.format(text.apply(quantity), arguments: _*)

    def apply(arguments: Seq[Any]): String = apply(Quantity.One, arguments)
  }

  implicit final class DictionaryOps(dictionary: Dictionary) {
    def apply(path: Path, quantity: Quantity, arguments: Seq[Any]): String =
      dictionary.get(path).map(_.apply(quantity, arguments)).getOrElse(path.printPretty)

    def apply(path: Path, arguments: Seq[Any]): String = apply(path, Quantity.One, arguments)

    def apply(path: Path, quantity: Quantity): String =
      dictionary.get(path).map(_.apply(quantity)).getOrElse(path.printPretty)

    def apply(path: Path): String = dictionary.get(path).map(_.apply(Quantity.One)).getOrElse(path.printPretty)
  }

  implicit final class I18nOps(i18n: I18n) {
    def apply(path: Path, locale: Locale, quantity: Quantity, arguments: Seq[Any]): String =
      i18n.get(path, locale).map(_.apply(quantity, arguments)).getOrElse(path.printPretty)

    def apply(path: Path, locale: Locale, arguments: Seq[Any]): String = apply(path, locale, Quantity.One, arguments)

    def apply(path: Path, locale: Locale, quantity: Quantity): String =
      i18n.get(path, locale).map(_.apply(quantity)).getOrElse(path.printPretty)

    def apply(path: Path, locale: Locale): String = apply(path, locale, Quantity.One)
  }
}
