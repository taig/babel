---
layout: home
title:  "Home"
section: "home"
---

[![CircleCI](https://circleci.com/gh/Taig/lokal/tree/master.svg?style=shield)](https://circleci.com/gh/Taig/lokal/tree/master)
[![codecov](https://codecov.io/gh/Taig/lokal/branch/master/graph/badge.svg)](https://codecov.io/gh/Taig/lokal)
[![Maven Central](https://img.shields.io/maven-central/v/io.taig/lokal_2.12.svg)](https://index.scala-lang.org/taig/lokal)

hello world

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