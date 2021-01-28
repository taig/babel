# Argument passing

Babel has helpers like `StringFormat1`, `StringFormat2`, etc. to help you insert arguments into Strings. In that regard it is similar to `java.lang.String.format` or `java.text.MessageFormat.format` but provides better compile time checks.

Babel's `StringFormat` does not automatically render specific types (as `String.format` does for numeric values). It only accepts `String` values, so the caller is responsible for converting the value to a renderable format beforehand.

en.conf
: @@snip [en.conf](/modules/documentation/resources/arguments/en.conf)

de.conf
: @@snip [de.conf](/modules/documentation/resources/arguments/de.conf)

@@@ note

In the German version, the parameters are called in swapped order.

@@@

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

final case class I18n(weather: StringFormat2)

val i18ns = Blocker[IO].use { blocker =>
  Loader
    .default[IO](blocker)
    .load("arguments", Set(Locales.en, Locales.de))
    .map(Decoder[I18n].decodeAll)
    .rethrow
    .map(_.toDictionary(Locales.en))
    .flatMap(_.liftTo[IO](new IllegalStateException("Translations for en missing")))
}.unsafeRunSync()
```

```scala mdoc
i18ns(Locales.en).weather("Cape Town", "26")
```

```scala mdoc
i18ns(Locales.de).weather("Frankfurt am Main", "18")
```

## Compile-time guarantees

In the above example we have used `StringFormat2`, which means that exactly 2 arguments must be passed. Otherwise a compile error will occur.

```scala mdoc:fail
i18ns(Locales.en).weather("Stockholm")
```

However, it is possible not to use all parameters when defining the translation.

```scala mdoc
Decoder[StringFormat2]
  .decode(Babel.text("It's always rainy in $1"))
  .map(_.apply("London", "3"))
```