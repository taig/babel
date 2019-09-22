---
layout: home
title:  "Home"
section: "home"
---

# Lokal

An internationalization (_i18n_) and localization (_l10n_) library for Scala and ScalaJS. A minimalistic API helps defining and evaluating translations on both, the server and the client. In its current early state, _Lokal_ does unfortunately include all translations into the generated JavaScript and does not support Ajax-loading the user's preferred language only.

# Installation

_Lokal_ is available for Scala `2.12` and `2.11` via Maven Central.


```scala
libraryDependencies += "@ORGANIZATION@" %%% "@MODULE@" % "@VERSION@"
```

# Getting started

```tut:reset:silent
import io.taig.lokal._
import io.taig.lokal.implicits._

val world: Translation[String] = en"World" & de"Welt"
val greeting: Translation[String] =
    en"Hello $world" & de"Hallo $world" & de_AT"Grüß Gott $world"
```

Exact matches are always preferred

```tut
greeting(Locales.de_AT)
```

Language is used as fallback when there is no specific translation for the country

```tut
greeting(Locales.de_DE)
```

The first translation is the fallback when the language is not available

```tut
greeting(Locales.es)
```