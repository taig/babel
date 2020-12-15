# Lokal

> String-based internationalisation (i18n) for Scala applications

## Features

- First class support for plurals
- Great for Scala.js, since `java.util.Locale` is not used
- Define your translations in code or in a supported serialisation format (e.g. JSON)
- Lift translations into data classes to improve type safety and enforce the translations in all languages
- Create subsets of your translations that are only suitable for a specific language, which can be useful for clients that request all translations for a single language 
- Pick your preferred String formatting flavour (e.g. `String.format` or `java.util.MessageFormat`) or bring your own
- Dependency-free `core` module

## Installation

Dependency-free core module, that contains all data class definitions, type classes and pre-defined locales

```scala
"io.taig" %%% "lokal-core" % "x.y.z" 
```

JSON serialisation and deserialisation

```scala
"io.taig" %%% "lokal-circe" % "x.y.z"
```

Codecs for translation case classes

```scala
"io.taig" %%% "lokal-generic" % "x.y.z"
```

String formatting choices: note that `java.util.MessageFormat` is not available for Scala.js, so the `printf` module is recommended

```scala
"io.taig" %%% "lokal-formatter-printf" % "x.y.z"
"io.taig" %% "lokal-formatter-message-format" % "x.y.z"
```

## Overview

### Data classes

- Lorem

### Type classes

- Ipsum