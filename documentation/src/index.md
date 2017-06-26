---
layout: home
title:  "Home"
section: "home"
---

[![CircleCI](https://circleci.com/gh/Taig/lokal/tree/master.svg?style=shield)](https://circleci.com/gh/Taig/lokal/tree/master)
[![codecov](https://codecov.io/gh/Taig/lokal/branch/master/graph/badge.svg)](https://codecov.io/gh/Taig/lokal)
[![Maven Central](https://img.shields.io/maven-central/v/io.taig/lokal_2.12.svg)](https://index.scala-lang.org/taig/lokal)

# Lokal

An internationalization (_i18n_) and localization (_l10n_) library for Scala and ScalaJS. A minimalistic API helps defining and evaluating translations on both, the server and the client. In its current early state, _Lokal_ does unfortunately include all translations into the generated JavaScript and does not support Ajax-loading the user's preferred language only.

# Installation

_Lokal_ is available for Scala `2.12`, `2.11` and `2.10` via Maven Central.

```tut:invisible
import io.taig.lokal.Build._
```

```tut:evaluated
println {
    s"""
     |libraryDependencies += "$organization" %% "$normalizedName" % "$version"
     """.stripMargin.trim
}
```

# Getting started

```tut:reset
import io.taig.lokal._; import imports._

val de = de"Hallo"
val de_AT = de_AT"Grüß Gott"
val de_CH = de_CH"Hoi"

val translations = de & de_AT & de_CH

translations.translate( Identifier.de_DE )
```