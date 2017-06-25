---
layout: home
title:  "Home"
section: "home"
---

[![CircleCI](https://circleci.com/gh/Taig/lokal/tree/master.svg?style=shield)](https://circleci.com/gh/Taig/lokal/tree/master)
[![codecov](https://codecov.io/gh/Taig/lokal/branch/master/graph/badge.svg)](https://codecov.io/gh/Taig/lokal)
[![Maven Central](https://img.shields.io/maven-central/v/io.taig/lokal_2.12.svg)](https://index.scala-lang.org/taig/lokal)

# Lokal

Lorem Ipsum

# Installation

_Lokal_ is available for Scala `2.12`, `2.11` and `2.10` via Maven Central.

```scala
libraryDependencies += "$organization" %% "lokal" % "$version"
```

# Getting started

```tut:invisible
import io.taig.lokal._; import imports._
```

```tut
val de = de"Hallo"
val de_AT = de_AT"Grüß Gott"
val de_CH = de_CH"Hoi"

val translations = de & de_AT & de_CH
```

```tut
translations.translate( Identifier.de_DE )
```