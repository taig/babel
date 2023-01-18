import sbtcrossproject.{CrossProject, CrossType, Platform}
import scala.util.chaining._

val Version = new {
  val Cats = "2.9.0"
  val CatsEffect = "3.4.5"
  val Circe = "0.14.3"
  val DisciplineMunit = "1.0.9"
  val Http4s = "1.0.0-M38"
  val Java = "11"
  val Munit = "0.7.29"
  val MunitCatsEffect = "1.0.7"
  val Scala213 = "2.13.10"
  val Scala3 = "3.2.1"
  val ScalajsDom = "2.3.0"
  val Sconfig = "1.5.0"
  val Shapeless = "2.3.10"
  val Slf4j = "2.0.5"
}

def module(
    identifier: Option[String],
    platforms: List[Platform] = List(JVMPlatform, JSPlatform),
    crossType: CrossType = CrossType.Pure
): CrossProject = {
  val path = identifier.map(_.split('-').mkString("/")).fold(".")("modules/" + _)
  CrossProject(identifier.getOrElse("root"), file(path))(platforms: _*)
    .crossType(crossType)
    .pipe { builder =>
      platforms match {
        case platform :: Nil => builder.withoutSuffixFor(platform)
        case _               => builder
      }
    }
    .build()
    .settings(
      name := "babel" + identifier.fold("")("-" + _),
      scalacOptions ++= {
        if (scalaVersion.value == Version.Scala3 && crossProjectPlatform.value == JSPlatform) "-scalajs" :: Nil
        else Nil
      }
    )
}

ThisBuild / scalaVersion := Version.Scala3
ThisBuild / crossScalaVersions := Version.Scala213 :: Version.Scala3 :: Nil

ThisBuild / developers := List(Developer("taig", "Niklas Klein", "mail@taig.io", url("https://taig.io/")))
ThisBuild / dynverVTagPrefix := false
ThisBuild / homepage := Some(url("https://github.com/taig/babel/"))
ThisBuild / licenses := List("MIT" -> url("https://raw.githubusercontent.com/taig/babel/main/LICENSE"))
ThisBuild / organization := "io.taig"
ThisBuild / organizationHomepage := Some(url("https://taig.io/"))
ThisBuild / versionScheme := Some("early-semver")

lazy val root = module(identifier = None, platforms = List(JVMPlatform))
  .enablePlugins(BlowoutYamlPlugin)
  .settings(noPublishSettings)
  .settings(
    blowoutGenerators ++= {
      val workflows = (LocalRootProject / baseDirectory).value / ".github" / "workflows"

      BlowoutYamlGenerator.lzy(workflows / "main.yml", GithubActionsGenerator.main(Version.Java)) ::
        BlowoutYamlGenerator.lzy(workflows / "pull-request.yml", GithubActionsGenerator.pullRequest(Version.Java)) ::
        Nil
    }
  )
  .aggregate(core, cats, loader, generic, circe, documentation, tests, sampleCore, sampleBackend, sampleFrontend)

lazy val core = module(identifier = Some("core")).settings(
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
  }.taskValue
)

lazy val cats = module(identifier = Some("cats"))
  .settings(
    libraryDependencies ++=
      "org.typelevel" %%% "cats-core" % Version.Cats ::
        Nil
  )
  .dependsOn(core)

lazy val loader = module(identifier = Some("loader"))
  .settings(
    libraryDependencies ++=
      "org.ekrich" %%% "sconfig" % Version.Sconfig ::
        "org.typelevel" %%% "cats-effect-kernel" % Version.CatsEffect ::
        Nil
  )
  .dependsOn(core)

lazy val generic = module(identifier = Some("generic"))
  .settings(
    libraryDependencies ++=
      (if (scalaVersion.value == Version.Scala3) Nil else "com.chuusai" %%% "shapeless" % Version.Shapeless :: Nil)
  )
  .dependsOn(core)

lazy val circe = module(identifier = Some("circe"))
  .settings(
    libraryDependencies ++=
      "io.circe" %%% "circe-parser" % Version.Circe ::
        Nil
  )
  .dependsOn(core)

lazy val documentation = module(identifier = Some("documentation"), platforms = List(JVMPlatform))
  .enablePlugins(MdocPlugin, ParadoxPlugin)
  .settings(noPublishSettings)
  .settings(
    Compile / paradox / sourceDirectory := mdocOut.value,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".." / "resources",
    libraryDependencies ++=
      "org.http4s" %% "http4s-ember-server" % Version.Http4s ::
        Nil,
    libraryDependencySchemes ++=
      "org.typelevel" %% "cats-effect" % "always" ::
        Nil,
    mdocIn := baseDirectory.value / ".." / "src",
    mdocVariables := Map(
      "ORGANIZATION" -> organization.value,
      "ARTIFACT" -> "babel",
      "VERSION" -> version.value
    ),
    paradox := (Compile / paradox).dependsOn(mdoc.toTask("")).value,
    paradoxTheme := Some(builtinParadoxTheme("generic"))
  )
  .dependsOn(core, loader, generic)

lazy val tests = module(identifier = Some("tests"), crossType = CrossType.Full)
  .settings(noPublishSettings)
  .settings(
    libraryDependencies ++=
      "org.scalameta" %%% "munit" % Version.Munit % "test" ::
        "org.typelevel" %%% "cats-laws" % Version.Cats % "test" ::
        "org.typelevel" %%% "discipline-munit" % Version.DisciplineMunit % "test" ::
        "org.typelevel" %%% "munit-cats-effect-3" % Version.MunitCatsEffect % "test" ::
        "org.scalameta" %%% "munit-scalacheck" % Version.Munit % "test" ::
        Nil,
    testFrameworks += new TestFramework("munit.Framework")
  )
  .dependsOn(cats, generic, loader)

lazy val sampleCore = module(identifier = Some("sample-core"))
  .settings(noPublishSettings)
  .dependsOn(core, generic)

lazy val sampleBackend = module(identifier = Some("sample-backend"), platforms = List(JVMPlatform))
  .enablePlugins(SbtWeb)
  .settings(noPublishSettings)
  .settings(
    Assets / pipelineStages := Seq(scalaJSPipeline),
    Compile / compile := ((Compile / compile) dependsOn scalaJSPipeline).value,
    Runtime / managedClasspath += (Assets / packageBin).value,
    libraryDependencies ++=
      "org.http4s" %% "http4s-dsl" % Version.Http4s ::
        "org.http4s" %% "http4s-ember-server" % Version.Http4s ::
        "org.slf4j" % "slf4j-simple" % Version.Slf4j ::
        Nil,
    scalaJSProjects := Seq(sampleFrontend.js)
  )
  .dependsOn(sampleCore, loader, circe)

lazy val sampleFrontend = module(identifier = Some("sample-frontend"), platforms = List(JSPlatform))
  .enablePlugins(ScalaJSWeb)
  .settings(noPublishSettings)
  .settings(
    libraryDependencies ++=
      "org.scala-js" %%% "scalajs-dom" % Version.ScalajsDom ::
        "org.typelevel" %%% "cats-effect" % Version.CatsEffect ::
        Nil,
    scalaJSUseMainModuleInitializer := true
  )
  .dependsOn(sampleCore, circe)

addCommandAlias("start", s";${sampleFrontend.js.id}/fastOptJS;${sampleBackend.jvm.id}/reStart")
addCommandAlias("stop", s"${sampleBackend.jvm.id}/reStop")
