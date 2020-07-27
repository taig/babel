import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

import scala.sys.process.Process
import scala.util.Try

val CatsVersion = "2.1.1"
val ScalacheckShapelessVersion = "1.2.5"
val CatsTestkitScalatestVersion = "2.0.0"

lazy val lokal = project
  .in(file("."))
  .settings(Settings.common ++ noPublishSettings)
  .settings(
    description := "i18n & l10n for (isomorphic) Scala applications"
  )
  .aggregate(core.jvm, core.js, dsl.jvm, dsl.js)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(Settings.common ++ sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "org.typelevel" %%% "cats-core" % CatsVersion ::
        "com.github.alexarchambault" %%% "scalacheck-shapeless_1.14" % ScalacheckShapelessVersion % "test" ::
        "org.typelevel" %%% "cats-testkit-scalatest" % CatsTestkitScalatestVersion % "test" ::
        Nil
  )

lazy val dsl = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(Settings.common ++ sonatypePublishSettings)
  .settings(
    sourceGenerators in Compile += Def.task {
      val pkg = s"${organization.value}.lokal"
      val languages = (sourceManaged in Compile).value / "Languages.scala"
      IO.write(languages, SourceGenerator.languages(pkg))
      val countries = (sourceManaged in Compile).value / "Countries.scala"
      IO.write(countries, SourceGenerator.countries(pkg))
      val locales = (sourceManaged in Compile).value / "Locales.scala"
      IO.write(locales, SourceGenerator.locales(pkg))
      val contexts = (sourceManaged in Compile).value / "LokalStringContexts.scala"
      IO.write(contexts, SourceGenerator.contexts(pkg))
      Seq(languages, countries, locales, contexts)
    }.taskValue
  )
  .dependsOn(core % "compile->compile;test->test")

lazy val website = project
  .enablePlugins(MicrositesPlugin)
  .settings(Settings.common ++ micrositeSettings)
  .settings(
    mdocVariables ++= {
      val dropMinor: String => String =
        version => s"`${version.replaceAll("\\.\\d+$", "")}`"

      Map(
        "MODULE_CORE" -> (core.jvm / name).value,
        "MODULE_DSL" -> (dsl.jvm / name).value,
        "SCALA_VERSIONS" -> crossScalaVersions.value
          .map(dropMinor)
          .mkString(", "),
        "SCALAJS_VERSION" -> dropMinor(scalaJSVersion)
      )
    },
    micrositeAnalyticsToken := "UA-64109905-2",
    micrositeDescription := (lokal / description).value
  )
  .dependsOn(dsl.jvm)
