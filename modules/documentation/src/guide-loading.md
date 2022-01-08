# Loading translations

## Defining translations in HOCON files

For each language a separate file must be created in the `resources` folder and the name of the `Locale` as filename. It is recommended to create a subdirectory for your language files, in the examples below we picked `babel`.


en.conf
: @@snip [en.conf](/modules/documentation/resources/babel/en.conf)

de.conf
: @@snip [de.conf](/modules/documentation/resources/babel/de.conf)

de-AT.conf
: @@snip [de-AT.conf](/modules/documentation/resources/babel/de-AT.conf)

## Loading translations into a `Babel`

```scala mdoc:to-string
import cats.effect._
import cats.effect.unsafe.implicits.global
import io.taig.babel._

val babels = Loader.default[IO]
  .load("babel", Set(Locales.en, Locales.de, Locales.de_AT))
  .unsafeRunSync()
```

## Decoding the `Babel` into a data class

```scala mdoc:to-string
import cats.syntax.all._
import io.taig.babel.generic.auto._

final case class I18n(greeting: String)

object I18n {
  implicit val decoder: Decoder[I18n] = deriveDecoder[I18n]
}

val i18ns = IO.fromEither(Decoder[I18n].decodeAll(babels))
  .map(_.withFallback(Locales.en))
  .flatMap(_.liftTo[IO](new IllegalStateException("Translations for en missing")))
  .unsafeRunSync()
```

```scala mdoc
i18ns(Locales.de).greeting
```

```scala mdoc
i18ns(Locales.de_AT).greeting
```

```scala mdoc
i18ns(Locales.de_CH).greeting
```

```scala mdoc
i18ns(Locales.fr).greeting
```