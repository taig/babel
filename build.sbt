import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val Version = new {
  val Cats = "2.2.0"
  val ScalacheckShapeless = "1.2.5"
  val CatsTestkitScalatest = "2.1.0"
}

noPublishSettings

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(Settings.common ++ sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "org.typelevel" %%% "cats-core" % Version.Cats ::
        "com.github.alexarchambault" %%% "scalacheck-shapeless_1.14" % Version.ScalacheckShapeless % "test" ::
        "org.typelevel" %%% "cats-testkit-scalatest" % Version.CatsTestkitScalatest % "test" ::
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
        "MODULE_CORE" -> (core.jvm / normalizedName).value,
        "MODULE_DSL" -> (dsl.jvm / normalizedName).value,
        "SCALA_VERSIONS" -> crossScalaVersions.value.map(dropMinor).mkString(", "),
        "SCALAJS_VERSION" -> dropMinor(scalaJSVersion)
      )
    },
    micrositeAnalyticsToken := "UA-64109905-2",
    micrositeDescription := "i18n & l10n for (isomorphic) Scala applications"
  )
  .dependsOn(dsl.jvm)
