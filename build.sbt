import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val Version = new {
  val Cats = "2.7.0"
  val CatsEffect = "3.3.11"
  val Circe = "0.14.1"
  val DisciplineMunit = "1.0.9"
  val Http4s = "1.0.0-M32"
  val Java = "11"
  val Munit = "0.7.29"
  val MunitCatsEffect = "1.0.7"
  val ScalajsDom = "2.1.0"
  val Sconfig = "1.4.9"
  val Shapeless = "2.3.9"
  val Slf4j = "1.7.36"

  val Scala213 = "2.13.8"
  val Scala3 = "3.1.1"
}

noPublishSettings

ThisBuild / scalaVersion := Version.Scala3
ThisBuild / crossScalaVersions := Version.Scala213 :: Version.Scala3 :: Nil

ThisBuild / developers := List(Developer("taig", "Niklas Klein", "mail@taig.io", url("https://taig.io/")))
ThisBuild / dynverVTagPrefix := false
ThisBuild / homepage := Some(url("https://github.com/taig/babel/"))
ThisBuild / licenses := List("MIT" -> url("https://raw.githubusercontent.com/taig/babel/main/LICENSE"))
ThisBuild / organization := "io.taig"
ThisBuild / organizationHomepage := Some(url("https://taig.io/"))
ThisBuild / versionScheme := Some("early-semver")

enablePlugins(BlowoutYamlPlugin)

blowoutGenerators ++= {
  val workflows = baseDirectory.value / ".github" / "workflows"

  BlowoutYamlGenerator(workflows / "main.yml", () => GithubActionsGenerator.main(Version.Java)) ::
    BlowoutYamlGenerator(workflows / "pull-request.yml", () => GithubActionsGenerator.pullRequest(Version.Java)) ::
    Nil
}

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/core"))
  .settings(
    Compile / sourceGenerators += Def.task {
      val pkg = s"${organization.value}.babel"
      val languages = (Compile / sourceManaged).value / "Languages.scala"
      IO.write(languages, JavaLocalesGenerator.languages(pkg))
      val countries = (Compile / sourceManaged).value / "Countries.scala"
      IO.write(countries, JavaLocalesGenerator.countries(pkg))
      val locales = (Compile / sourceManaged).value / "Locales.scala"
      IO.write(locales, JavaLocalesGenerator.locales(pkg))
      val stringFormatN = (Compile / sourceManaged).value / "StringFormatN.scala"
      IO.write(stringFormatN, JavaLocalesGenerator.stringFormatN(pkg))
      Seq(languages, countries, locales, stringFormatN)
    }.taskValue,
    name := "babel-core"
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
        "org.typelevel" %%% "cats-effect-kernel" % Version.CatsEffect ::
        Nil,
    name := "babel-loader"
  )
  .dependsOn(core)

lazy val generic = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/generic"))
  .settings(
    libraryDependencies ++=
      (if (scalaVersion.value == Version.Scala3) Nil else "com.chuusai" %%% "shapeless" % Version.Shapeless :: Nil),
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
  .settings(noPublishSettings)
  .settings(
    libraryDependencies ++=
      "org.http4s" %% "http4s-blaze-server" % Version.Http4s ::
        Nil,
    libraryDependencySchemes ++=
      "org.typelevel" %% "cats-effect" % "always" ::
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
        "org.typelevel" %%% "munit-cats-effect-3" % Version.MunitCatsEffect % "test" ::
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
    libraryDependencySchemes ++=
      "org.typelevel" %% "cats-effect" % "always" ::
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
