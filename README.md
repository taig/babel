# Babel

> Internationalization (i18n) for Scala applications
> 
> [![GitLab CI](https://gitlab.com/slozzer/babel/badges/master/pipeline.svg?style=flat-square)](https://gitlab.com/slozzer/babel/pipelines)
[![Maven Central](https://img.shields.io/maven-central/v/net.slozzer/babel-core_2.13.svg?style=flat-square)](https://search.maven.org/search?q=g:net.slozzer%20AND%20a:babel-*)
[![License](https://img.shields.io/github/license/slozzer/babel?style=flat-square)](LICENSE)

## Features

- First class support for plurals
- Great fit for Scala.js clients, since `java.util.Locale` is not used
- Translation definitions in [HOCON](https://github.com/lightbend/config/blob/master/HOCON.md) format
- Decode translations into data classes
- Easily share translations of a specific language with the frontend in JSON format
- Typesafe alternative to `String.format` or `java.util.MessageFormat` argument injection
- Dependency-free `core` module

## Installation

Dependency-free core module, that contains all data class definitions, type classes and pre-defined locales

```scala
"net.slozzer" %%% "babel-core" % "x.y.z" 
```
Serialization formats

```scala
"net.slozzer" %%% "babel-circe" % "x.y.z"
```

Reading HOCON translation definitions from resources

```scala
"net.slozzer" %% "babel-loader" % "x.y.z"
```

Codecs to populate custom data classes from translation objects

```scala
"net.slozzer" %%% "babel-generic" % "x.y.z"
```

Default setup which is assumed in the documentation below

```scala
"net.slozzer" %%% "babel-circe" % "x.y.z"
"net.slozzer" %%% "babel-generic" % "x.y.z"
"net.slozzer" %%% "babel-loader" % "x.y.z"
```

## Guide

### Defining translations in HOCON files

For each language a separate file must be created in the `resources/babel` folder and the name of the `Locale` as filename.

`resources/babel/en.conf`

```hocon
greeting = "Good afternoon"
```

`resources/babel/de.conf`

```hocon
greeting = "Guten Tag"
```

`resources/babel/de-AT.conf`

```hocon
greeting = "Grüß Gott"
```

Use HOCON's advanced features to share and reuse translations

`resources/babel/*.conf`

```hocon
greeting = "Hi"
```

`resources/babel/es.conf`

```hocon
include "*"
```

### Loading translations into a `Babel`

```scala
import net.slozzer.babel.Loader

val babel = Loader.default[IO](blocker).load("babel", Set(Locales.en, Locales.de, Locales.de_AT)).unsafeRunSync()
```

```
> Translations(Locales.en -> ..., Locales.de -> ..., Locales.de_AT -> ...)
```

### Decoding the `Babel` into a data class

```scala
import net.slozzer.babel.generic.auto._
import net.slozzer.babel.{Decoder, Encoder, Quantities, StringFormat1}

final case class I18n(greeting: String)

object I18n {
  implicit val decoder: Decoder[I18n] = deriveDecoder[I18n]
}

val i18ns = Loader.default[IO](blocker)
  .load("babel", Set(Locales.en, Locales.de, Locales.de_AT))
  .map(Decoder[I18n].decodeAll)
  .rethrow
  .flatMap { translations =>
    translations
      .get(Locales.en)
      .liftTo[F](new IllegalStateException("Translations for en missing"))
      .map(fallback => (translations - Locales.en).toDictionary(fallback))
  }
```


```scala
i18ns(Locales.de).greeting
```

```
> "Guten Tag"
```

```scala
i18ns(Locales.de_AT).greeting
```

```
> "Grüß Gott"
```

```scala
i18ns(Locales.de_CH).greeting
```

```
> "Guten Tag"
```

```scala
i18ns(Locales.fr).greeting
```

```
> "Good afternoon"
```

## Sample

Take a look at the client / server sample application that aims to showcase the library's features.

## Cookbook

### Resolve the user's preferred language from the `Accept-Language` header in an http4s middleware

Uses Java's `Locale.LanguageRange.parse` to lift the `Accept-Language` header value into an ordered list of `Locale`s and aligns it with the `Locale`s that are supported by the application.

```scala
import java.util.{Locale => JavaLocale}

import scala.jdk.CollectionConverters._

import cats.Defer
import net.slozzer.babel.Locale
import org.http4s.HttpRoutes
import org.http4s.headers.`Accept-Language`

final class LocalesMiddleware[F[_]: Defer](locales: Set[Locale], fallback: Locale) {
  def apply(routes: Locale => HttpRoutes[F]): HttpRoutes[F] =
    HttpRoutes[F] { request =>
      val locale = request.headers
        .get(`Accept-Language`)
        .map(_.value)
        .flatMap { value =>
          JavaLocale.LanguageRange
            .parse(value)
            .asScala
            .sortWith(_.getWeight > _.getWeight)
            .map(language => JavaLocale.forLanguageTag(language.getRange))
            .flatMap(Locale.fromJavaLocale)
            .find(locales.contains)
        }
        .getOrElse(fallback)

      routes(locale).run(request)
    }
}
```

<hr />

<a href="https://slozzer.net/">
  <img src="https://i.imgur.com/zJlOKhO.png" width="300" alt="slozzer: Mediation of certified locksmiths for emergency door opening with fixed price guarantee" />
</a>

Babel is developed and maintained under the [slozzer](https://slozzer.net/) organization.