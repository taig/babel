---
layout: home
title:  "Home"
section: "home"
---

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