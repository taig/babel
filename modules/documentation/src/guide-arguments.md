# Argument passing

Babel has helpers like `StringFormat1`, `StringFormat2`, etc. to help you insert arguments into Strings. In that regard it is similar to `java.lang.String.format` or `java.text.MessageFormat.format` but provides better compile time checks.

Babel's `StringFormat` does not automatically render arbitrary types (as `String.format` does for numeric values). It only accepts `String` values, so the caller is responsible for converting the value to a renderable format beforehand.

- Parameter placeholders are encoded in the style of `java.text.MessageFormat`, starting at index `0` (e.g. `{0}`, `{1}`, ...)
- Each placeholder index may be used at most once (e.g. `"lorem {0} dolar {0}"` is not allowed)
- The order of the placeholders may vary across translations (e.g. "The {0} {1} is fast" and "El {1} {0} es rapido")
- There can not be more placeholders than arguments (e.g. `StringFormat1` can not be decoded when there is `{0}` and `{1}` as there is only 1 argument)
- It is a decoding error to define placeholder indices that exceed the `StringFormatN` arity (e.g. `StringFormat2` may only use `{0}` and `{1}`, but not `{2}`)
- Not all placeholders must be use (e.g. "Good afternoon, {0}. Today it's {1} degrees." and "Guten Tag, sehr geehrte Damen und Herren. Heute sind es {1} Grad.")

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

```scala mdoc:to-string
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
    .map(_.withFallback(Locales.en))
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

However, it is allowed not to make use of all parameters when defining the translation.

```scala mdoc
Decoder[StringFormat2]
  .decode(Babel.text("It's always rainy in {0}"))
  .map(_.apply("London", "3"))
```