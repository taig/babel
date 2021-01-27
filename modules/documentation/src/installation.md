# Installation

Dependency-free core module, that contains all data class definitions, type classes and pre-defined locales

```scala
"@ORGANIZATION@" %%% "@ARTIFACT@-core" % "@VERSION@" 
```

Cats type class instances

```scala
"@ORGANIZATION@" %%% "@ARTIFACT@-cats" % "@VERSION@"
```

Serialization formats

```scala
"@ORGANIZATION@" %%% "@ARTIFACT@-circe" % "@VERSION@"
```

Reading HOCON translation definitions from resources

```scala
"@ORGANIZATION@" %%% "@ARTIFACT@-loader" % "@VERSION@"
```

Codecs to populate custom data classes from translation objects

```scala
"@ORGANIZATION@" %%% "@ARTIFACT@-generic" % "@VERSION@"
```

Default setup which is assumed in the documentation below

```scala
"@ORGANIZATION@" %%% "@ARTIFACT@-circe" % "@VERSION@"
"@ORGANIZATION@" %%% "@ARTIFACT@-generic" % "@VERSION@"
"@ORGANIZATION@" %%% "@ARTIFACT@-loader" % "@VERSION@"
```