import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

lazy val lokal = project
  .in(file("."))
  .settings(Settings.common ++ noPublishSettings)
  .aggregate(core.jvm, core.js, dsl.jvm, dsl.js)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(Settings.common ++ sonatypePublishSettings)
  .settings(
    description := "i18n & l10n for (isomorphic) Scala applications",
    libraryDependencies ++=
      "org.typelevel" %%% "cats-core" % "2.0.0" ::
        "org.typelevel" %%% "cats-testkit-scalatest" % "1.0.0-M2" % "test" ::
        Nil,
    name := "lokal-core"
  )

lazy val dsl = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(Settings.common ++ sonatypePublishSettings)
  .settings(
    name := "lokal-dsl",
    sourceGenerators in Compile += Def.task {
      val pkg = s"${organization.value}.lokal"
      val languages = (sourceManaged in Compile).value / "Languages.scala"
      IO.write(languages, SourceGenerator.languages(pkg))
      val countries = (sourceManaged in Compile).value / "Countries.scala"
      IO.write(countries, SourceGenerator.countries(pkg))
      val locales = (sourceManaged in Compile).value / "Locales.scala"
      IO.write(locales, SourceGenerator.locales(pkg))
      Seq(languages, countries, locales)
    }.taskValue
  )
  .dependsOn(core)

lazy val website = project
  .enablePlugins(MicrositesPlugin)
  .settings(Settings.common ++ micrositeSettings)
  .settings(
    mdocVariables ++= Map(
      "MODULE" -> (normalizedName in core.jvm).value,
      "ORGANIZATION" -> organization.value,
      "VERSION" -> version.value,
      "SCALA_VERSIONS" -> crossScalaVersions.value.mkString(", "),
      "SCALAJS_VERSION" -> scalaJSVersion
    ),
    micrositeAnalyticsToken := "UA-64109905-2",
    micrositeDescription := (core.jvm / description).value,
    micrositeName := (core.jvm / name).value
  )
  .dependsOn(core.jvm)
