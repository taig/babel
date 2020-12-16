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

String formatting choices, pick one or role your own

> Please note that `java.util.MessageFormat` is not available for Scala.js, so the `printf` module is recommended

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
Basic data classes in the fashion of `java.util.Locale`. It is possible to convert between `java.util.Locale` and `io.taig.babel.Locale`.
- `Text`, `Quantity`  
Lorem ipsum
- `Segments`, `Path`  
`Segments` is a tree structure that mimics nested case classes. A `Path` can be used to identify a `Node` or a `Leaf` in such a tree.
- `Dictionary`  
A collection of `Text`s that are namespaced in `Segments`
- `Translation`  
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
