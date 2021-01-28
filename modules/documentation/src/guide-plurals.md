# Plurals

Babel provides first class support for plurals with the `Quantities` class.

en.conf
: @@snip [en.conf](/modules/documentation/resources/plurals/en.conf)

```scala mdoc:invisible
import cats.effect.{ContextShift, IO}
import scala.concurrent.ExecutionContext.global

implicit val contextShift: ContextShift[IO] = IO.contextShift(global)
```

```scala mdoc
import cats.effect._
import cats.syntax.all._
import net.slozzer.babel._
import net.slozzer.babel.generic.auto._

final case class I18n(bicycles: Quantities[String])

val i18n = Blocker[IO].use { blocker =>
  Loader
    .default[IO](blocker)
    .load("plurals", Set(Locales.en))
    .map(Decoder[I18n].decodeAll)
    .rethrow
    .map(_.toDictionary(Locales.en))
    .flatMap(_.liftTo[IO](new IllegalStateException("Translations for en missing")))
    .map(_.apply(Locales.en))
}.unsafeRunSync()
```

```scala mdoc
i18n.bicycles(0)
```

```scala mdoc
i18n.bicycles(1)
```

```scala mdoc
i18n.bicycles(100)
```

## Using plurals with arguments

Of course, you will most likely want to reference the given quantity in your translations, which was painfully omitted in the example above. This can be achieved by combining the `StringFormat` feature with `Quantities`.

en.conf
: @@snip [en.conf](/modules/documentation/resources/plurals-arguments/en.conf)

```scala mdoc:invisible:reset
import cats.effect.{ContextShift, IO}
import scala.concurrent.ExecutionContext.global

implicit val contextShift: ContextShift[IO] = IO.contextShift(global)
```

```scala mdoc
import cats.effect._
import cats.syntax.all._
import net.slozzer.babel._
import net.slozzer.babel.generic.auto._

final case class I18n(bicycles: Quantities[String])

val i18n = Blocker[IO].use { blocker =>
  Loader
    .default[IO](blocker)
    .load("plurals-arguments", Set(Locales.en))
    .map(Decoder[I18n].decodeAll)
    .rethrow
    .map(_.toDictionary(Locales.en))
    .flatMap(_.liftTo[IO](new IllegalStateException("Translations for en missing")))
    .map(_.apply(Locales.en))
}.unsafeRunSync()
```

```scala mdoc
i18n.bicycles(0)
```

```scala mdoc
i18n.bicycles(9000000)
```