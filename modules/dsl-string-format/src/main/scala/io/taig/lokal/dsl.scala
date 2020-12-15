package io.taig.lokal

object dsl {
  implicit final class TextOps(val text: Text) extends AnyVal {
    def apply(quantity: Quantity, arguments: Seq[Any]): String = String.format(text.apply(quantity), arguments: _*)

    def apply(arguments: Seq[Any]): String = apply(Quantity.One, arguments)
  }

  implicit final class DictionaryOps(dictionary: Dictionary) {
    def apply(key: String, quantity: Quantity, arguments: Seq[Any]): String =
      dictionary.get(key).map(_.apply(quantity, arguments)).getOrElse(key)

    def apply(key: String, arguments: Seq[Any]): String = apply(key, Quantity.One, arguments)

    def apply(key: String, quantity: Quantity): String = dictionary.get(key).map(_.apply(quantity)).getOrElse(key)

    def apply(key: String): String = dictionary.get(key).map(_.apply(Quantity.One)).getOrElse(key)
  }

  implicit final class I18nOps(i18n: I18n) {
    def apply(key: String, locale: Locale, quantity: Quantity, arguments: Seq[Any]): String =
      i18n.get(key, locale).map(_.apply(quantity, arguments)).getOrElse(key)

    def apply(key: String, locale: Locale, arguments: Seq[Any]): String = apply(key, locale, Quantity.One, arguments)

    def apply(key: String, locale: Locale, quantity: Quantity): String =
      i18n.get(key, locale).map(_.apply(quantity)).getOrElse(key)

    def apply(key: String, locale: Locale): String = apply(key, locale, Quantity.One)
  }
}
