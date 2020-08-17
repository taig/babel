---
layout: home
title:  "Home"
section: "home"
---

# @NAME@

[![GitLab CI](https://gitlab.com/taig-github/lokal/badges/master/build.svg?style=flat-square)](https://gitlab.com/taig-github/lokal/pipelines)
[![GitLab CI](https://gitlab.com/taig-github/lokal/badges/master/coverage.svg?style=flat-square)](https://lokal.taig.io/coverage)
[![Maven Central](https://img.shields.io/maven-central/v/io.taig/lokal-core_2.13.svg?style=flat-square)](https://search.maven.org/search?q=g:io.taig%20AND%20a:lokal-*)
[![License](https://img.shields.io/github/license/taig/lokal?style=flat-square)](LICENSE)

An internationalization (_i18n_) and localization (_l10n_) library for Scala and ScalaJS. A minimalistic API helps defining and evaluating translations on both, the server and the client. In its current early state, _@NAME@_ does unfortunately include all translations into the generated JavaScript and does not support Ajax-loading the user's preferred language only.

## Installation

_@NAME@_ is available for Scala @SCALA_VERSIONS@ and Scala.js @SCALAJS_VERSION@ via Maven Central.


```scala
libraryDependencies ++=
  "@ORGANIZATION@" %%% "@MODULE_CORE@" % "@VERSION@" ::
  "@ORGANIZATION@" %%% "@MODULE_DSL@" % "@VERSION@" ::
  Nil
```

## Usage

_@NAME@'s_ main feature is the `Translation` class. It is intended to be used with the `@MODULE_DSL@` module with `String` values.

```scala mdoc:silent
import io.taig.lokal._
import io.taig.lokal.dsl._

val world: Translation[String] = en"World" & de"Welt"
val greeting: Translation[String] = en_GB"Hello $world" & de"Hallo $world" & de_AT"Grüß Gott $world"
```

Exact matches are always preferred

```scala mdoc
greeting.translate(Locales.de_AT)
greeting(Locales.de_AT)
```

Language is used as fallback when there is no specific translation for the country

```scala mdoc
greeting.translate(Locales.de_DE)
greeting(Locales.de_DE)
```

### Universal Values

Translations can be promoted to be the same across all languages.

```scala mdoc:silent
val project: Translation[String] = Translation.universal("@NAME@")
val headline: Translation[String] = en"The name of this project is $project"
```

```scala mdoc
headline.translate(Locales.en)
headline.translate(Locales.fr)
project.translate(Locales.fr)
```

### Monadic composition

```scala mdoc:silent
import cats.implicits._

val currencySymbol: Translation[Char] =
  Translation.of(Locales.de, Locales.fr, Locales.es)('€') &
  Translation.one(Locales.en_GB, '£') &
  Translation.universal('$')

def formatNumber(value: Float): Translation[String] =
  Translation.universal(String.valueOf(value)).andThen { value =>
    Translation.of(Locales.de, Locales.fr, Locales.es)(value.replace(".", ",")) &
    Translation.universal(value)
  }

def price(value: Int): Translation[String] =
  (formatNumber(value / 100f), currencySymbol).mapN { (value, symbol) =>
    s"$value $symbol"
  }
```

```scala mdoc
price(99).translate(Locales.en_US)
price(149).translate(Locales.de)
```