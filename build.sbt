import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val Version = new {
  val Cats = "2.4.2"
  val CatsEffect = "2.3.2"
  val Circe = "0.13.0"
  val DisciplineMunit = "1.0.6"
  val Http4s = "0.21.19"
  val Munit = "0.7.22"
  val MunitCatsEffect = "0.13.0"
  val ScalajsDom = "1.1.0"
  val Sconfig = "1.4.0"
  val Shapeless = "2.3.3"
  val Slf4j = "1.7.30"
}

noPublishSettings

ThisBuild / scalaVersion := "2.13.4"

ThisBuild / dynverVTagPrefix := false
ThisBuild / homepage := Some(url("https://github.com/slozzer/babel/"))
ThisBuild / licenses := List("MIT" -> url("https://raw.githubusercontent.com/slozzer/babel/master/LICENSE"))
ThisBuild / organization := "net.slozzer"
ThisBuild / organizationHomepage := Some(url("https://slozzer.net/"))
ThisBuild / developers := List(
  Developer(
    "taig",
    "Niklas Klein",
    "mail@taig.io",
    url("https://taig.io/")
  )
)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/core"))
  .settings(
    name := "babel-core",
    sourceGenerators in Compile += Def.task {
      val pkg = s"${organization.value}.babel"
      val languages = (sourceManaged in Compile).value / "Languages.scala"
      IO.write(languages, SourceGenerator.languages(pkg))
      val countries = (sourceManaged in Compile).value / "Countries.scala"
      IO.write(countries, SourceGenerator.countries(pkg))
      val locales = (sourceManaged in Compile).value / "Locales.scala"
      IO.write(locales, SourceGenerator.locales(pkg))
      val stringFormatN = (sourceManaged in Compile).value / "StringFormatN.scala"
      IO.write(stringFormatN, SourceGenerator.stringFormatN(pkg))
      Seq(languages, countries, locales, stringFormatN)
    }.taskValue
  )

lazy val cats = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/cats"))
  .settings(
    libraryDependencies ++=
      "org.typelevel" %%% "cats-core" % Version.Cats ::
        Nil,
    name := "babel-cats"
  )
  .dependsOn(core)

lazy val loader = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/loader"))
  .settings(
    libraryDependencies ++=
      "org.ekrich" %%% "sconfig" % Version.Sconfig ::
        "org.typelevel" %%% "cats-effect" % Version.CatsEffect ::
        Nil,
    name := "babel-loader"
  )
  .dependsOn(core)

lazy val generic = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/generic"))
  .settings(
    libraryDependencies ++=
      "com.chuusai" %%% "shapeless" % Version.Shapeless ::
        Nil,
    name := "babel-generic"
  )
  .dependsOn(core)

lazy val circe = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/circe"))
  .settings(
    libraryDependencies ++=
      "io.circe" %%% "circe-parser" % Version.Circe ::
        Nil,
    name := "babel-circe"
  )
  .dependsOn(core)

lazy val documentation = project
  .in(file("modules/documentation"))
  .enablePlugins(MdocPlugin, ParadoxPlugin)
  .settings(
    libraryDependencies ++=
      "org.http4s" %% "http4s-blaze-server" % Version.Http4s ::
        Nil,
    mdocIn := sourceDirectory.value,
    mdocVariables := Map(
      "ORGANIZATION" -> organization.value,
      "ARTIFACT" -> "babel",
      "VERSION" -> version.value
    ),
    name := "babel-documentation",
    Compile / paradox / sourceDirectory := mdocOut.value,
    Compile / unmanagedResourceDirectories += baseDirectory.value / "resources",
    paradox := (Compile / paradox).dependsOn(mdoc.toTask("")).value,
    paradoxTheme := Some(builtinParadoxTheme("generic"))
  )
  .dependsOn(core.jvm, loader.jvm, generic.jvm)

lazy val tests = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/tests"))
  .settings(noPublishSettings)
  .settings(
    libraryDependencies ++=
      "org.scalameta" %%% "munit" % Version.Munit % "test" ::
        "org.typelevel" %%% "cats-laws" % Version.Cats % "test" ::
        "org.typelevel" %%% "discipline-munit" % Version.DisciplineMunit % "test" ::
        "org.typelevel" %%% "munit-cats-effect-2" % Version.MunitCatsEffect % "test" ::
        "org.scalameta" %%% "munit-scalacheck" % Version.Munit % "test" ::
        Nil,
    name := "babel-tests",
    testFrameworks += new TestFramework("munit.Framework")
  )
  .dependsOn(cats, generic, loader)
  .jsSettings(scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)))

lazy val sampleCore = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/sample/core"))
  .settings(noPublishSettings)
  .settings(
    name := "babel-sample-core"
  )
  .dependsOn(core, generic)

lazy val sampleBackend = project
  .in(file("modules/sample/backend"))
  .enablePlugins(SbtWeb)
  .settings(noPublishSettings)
  .settings(
    Assets / pipelineStages := Seq(scalaJSPipeline),
    Assets / WebKeys.packagePrefix := "public/",
    Compile / compile := ((Compile / compile) dependsOn scalaJSPipeline).value,
    libraryDependencies ++=
      "org.http4s" %% "http4s-dsl" % Version.Http4s ::
        "org.http4s" %% "http4s-blaze-server" % Version.Http4s ::
        "org.slf4j" % "slf4j-simple" % Version.Slf4j ::
        Nil,
    name := "babel-sample-backend",
    Runtime / managedClasspath += (Assets / packageBin).value,
    scalaJSProjects := Seq(sampleFrontend)
  )
  .dependsOn(sampleCore.jvm, loader.jvm, circe.jvm)

lazy val sampleFrontend = project
  .in(file("modules/sample/frontend"))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .settings(noPublishSettings)
  .settings(
    libraryDependencies ++=
      "org.scala-js" %%% "scalajs-dom" % Version.ScalajsDom ::
        "org.typelevel" %%% "cats-effect" % Version.CatsEffect ::
        Nil,
    name := "babel-sample-frontend",
    scalaJSUseMainModuleInitializer := true
  )
  .dependsOn(sampleCore.js, circe.js)

addCommandAlias("start", ";sampleFrontend/fastOptJS;sampleBackend/reStart")
