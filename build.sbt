import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

lazy val lokal = project
  .in(file("."))
  .settings(Settings.common ++ noPublishSettings)
  .aggregate(core.jvm, core.js)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(Settings.common ++ sonatypePublishSettings)
  .settings(
    description := "i18n & l10n for (isomorphic) Scala applications",
    libraryDependencies ++=
      "org.typelevel" %%% "cats-core" % "1.6.0" ::
        "io.github.cquiroz" %%% "scala-java-locales" % "0.3.16-cldr35" ::
        "org.scalatest" %%% "scalatest" % "3.0.5" % "test" ::
        "org.typelevel" %%% "cats-testkit" % "1.6.0" % "test" ::
        Nil,
    name := "lokal",
    sourceGenerators in Compile += Def.task {
      val pkg = s"${organization.value}.${normalizedName.value}"
      val source = SourceGenerator.render(pkg)
      val file = (sourceManaged in Compile).value / "Definitions.scala"
      IO.write(file, source)
      Seq(file)
    }.taskValue
  )

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
