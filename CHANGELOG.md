# Changelog

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