# Changelog

# 0.9.1

_2020-09-??_

 * Upgrade to cats 2.2.0

# 0.9.0

_2020-09-02_

 * Introduce Dictionary as a non-empty alternative to Translation
 * Add Translation.exact builder

# 0.8.0

_2020-08-30_

 * Back to monadic behavior from 0.6
 * Add fromJavaLocale / toJavaLocale methods

# 0.7.0

_2020-08-17_

 * [#22] Improve Show/toString of Translation (#71)
 * Upgrade to cats-testkit-scalatest 2.0.0
 * Upgrade to scala 2.12.12
 * Upgrade to scala 2.13.3
 * Upgrade to sbt-scalajs 1.1.1
 * Upgrade to sbt 1.3.13
 * Upgrade sbt-houserules to 0.2.4 (#69)

# 0.6.1

_2020-03-28_

 * Make Translation covariant
 * Upgrade to cats 2.1.1
 * Upgrade to sbt-scalajs 1.0.1
 * Upgrade to cats-testkit-scalatest 1.0.1
 * Upgrade to scala 2.12.11
 * Upgrade to scalacheck-shapeless_1.14 1.2.5
 * Update sbt-houserules to 0.1.11 (#57)
 * Update sbt to 1.3.8 (#46)
 * Drop Scala 2.11
 * Remove explicit dependency to sbt-microsites, as it is included in sbt-houserules
 * Disable coverage
 * Disable sbt-microsite multi version build
 * Move warnings and formatting checks into separate CI jobs

# 0.6.0

_2019-09-24_

 * Introduce `dsl` module that removes `Locale` presets from `core`
 * Remove `java.util.Locale` in favor of a lightweight custom data class
 * Lawful `Monad[Translation]` instance