import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

lazy val lokal = project
  .in(file("."))
  .settings(Settings.common ++ noPublishSettings)
  .settings(
    description := "i18n & l10n for (isomorphic) Scala applications",
    name := "Lokal",
    normalizedName := "lokal"
  )
  .aggregate(core.jvm, core.js, dsl.jvm, dsl.js)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(Settings.common ++ sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "org.typelevel" %%% "cats-core" % "2.0.0" ::
        "org.typelevel" %%% "cats-testkit-scalatest" % "1.0.0-M2" % "test" ::
        Nil,
    name := "Core"
  )

lazy val dsl = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(Settings.common ++ sonatypePublishSettings)
  .settings(
    name := "Dsl",
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
    name := "Website",
    mdocVariables ++= Map(
      "NAME" -> (lokal / name).value,
      "MODULE_CORE" -> (core.jvm / normalizedName).value,
      "MODULE_DSL" -> (dsl.jvm / normalizedName).value,
      "ORGANIZATION" -> organization.value,
      "VERSION" -> version.value,
      "SCALA_VERSIONS" -> crossScalaVersions.value.mkString(", "),
      "SCALAJS_VERSION" -> scalaJSVersion
    ),
    micrositeDocumentationLabelDescription := "Coverage",
    micrositeDocumentationUrl := "/coverage",
    micrositeBaseUrl := "",
    micrositeAnalyticsToken := "UA-64109905-2",
    micrositeDescription := (lokal / description).value,
    micrositeHomepage := "https://lokal.taig.io",
    micrositeName := (lokal / name).value,
    micrositeUrl := micrositeHomepage.value
  )
  .dependsOn(core.jvm)
