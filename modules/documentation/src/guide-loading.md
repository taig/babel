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

```scala mdoc:invisible
import cats.effect.{ContextShift, IO}
import scala.concurrent.ExecutionContext.global

implicit val contextShift: ContextShift[IO] = IO.contextShift(global)
```

```scala mdoc:to-string
import cats.effect._
import net.slozzer.babel._

val babels = Blocker[IO].use { blocker =>
  Loader.default[IO](blocker).load("babel", Set(Locales.en, Locales.de, Locales.de_AT))
}.unsafeRunSync()
```

## Decoding the `Babel` into a data class

```scala mdoc:to-string
import cats.syntax.all._
import net.slozzer.babel.generic.auto._
import net.slozzer.babel.{Decoder, Encoder, Quantities, StringFormat1}

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