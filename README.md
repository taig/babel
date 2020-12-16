# Babel

> String-based internationalization (i18n) for Scala applications

## Features

- First class support for plurals
- Great fit for Scala.js clients, since `java.util.Locale` is not used
- Translation definitions in either code or a supported serialisation format (e.g. JSON)
- Allows lifting translations into data classes: no more `String` key look-ups
- Creating translation subsets that are only suitable for a specific language, which can be useful for clients that request all translations for a single language 
- Open to arbitrary `String` formatting solutions (e.g. `String.format` or `java.util.MessageFormat`)
- Dependency-free `core` module

## Installation

Dependency-free core module, that contains all data class definitions, type classes and pre-defined locales

```scala
"io.taig" %%% "babel-core" % "x.y.z" 
```
JSON serialisation and deserialisation

```scala
"io.taig" %%% "babel-circe" % "x.y.z"
```

Reading serialised language definitions from resources (JVM only)

```scala
"io.taig" %% "babel-loader" % "x.y.z"
```

Codecs to populate custom translation data classes

```scala
"io.taig" %%% "babel-generic" % "x.y.z"
```

String formatting choices: note that `java.util.MessageFormat` is not available for Scala.js, so the `printf` module is recommended

```scala
"io.taig" %%% "babel-formatter-printf" % "x.y.z"
"io.taig" %% "babel-formatter-message-format" % "x.y.z"
```

Default setup which is assumed in the documentation below

```scala
"io.taig" %% "babel-loader" % "x.y.z"
"io.taig" %%% "babel-circe" % "x.y.z"
"io.taig" %%% "babel-generic" % "x.y.z"
"io.taig" %%% "babel-formatter-printf" % "x.y.z"
```

## Overview

### Data classes

- `Locale`, `Language`, `Country`  
Lorem ipsum
- `Text`, `Quantity`  
Lorem ipsum
- `Dictionary`  
Lorem ipsum
- `Translation`  
Lorem ipsum
- `Segments`, `Path`  
Lorem ipsum
- `Babel`  
Lorem ipsum

### Type classes

- `Formatter`  
Lorem ipsum
- `Printer`  
Lorem ipsum
- `Parser`  
Lorem ipsum
- `Encoder`  
Lorem ipsum
- `Decoder`  
Lorem ipsum
