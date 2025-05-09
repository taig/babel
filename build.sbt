import sbtcrossproject.{CrossProject, CrossType, Platform}
import scala.util.chaining._

val Version = new {
  val Cats = "2.13.0"
  val CatsEffect = "3.6.1"
  val Circe = "0.14.13"
  val DisciplineMunit = "2.0.0"
  val Http4s = "1.0.0-M44"
  val Java = "17"
  val Log4Cats = "2.7.0"
  val Munit = "1.1.1"
  val MunitCatsEffect = "2.1.0"
  val MunitScalacheck = "1.1.0"
  val Scala213 = "2.13.16"
  val Scala3 = "3.3.6"
  val ScalajsDom = "2.8.0"
  val Sconfig = "1.9.0"
  val Shapeless = "2.3.13"
  val Slf4j = "2.0.17"
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
      name := "babel" + identifier.fold("")("-" + _)
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
        BlowoutYamlGenerator.lzy(workflows / "tag.yml", GithubActionsGenerator.tag(Version.Java)) ::
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
  .enablePlugins(MdocPlugin, ParadoxPlugin, ParadoxMaterialThemePlugin)
  .settings(noPublishSettings)
  .settings(
    Compile / paradox / sourceDirectory := mdocOut.value,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".." / "resources",
    libraryDependencies ++=
      "org.http4s" %% "http4s-ember-server" % Version.Http4s ::
        Nil,
    mdocIn := baseDirectory.value / ".." / "src",
    mdocVariables := Map(
      "ORGANIZATION" -> organization.value,
      "ARTIFACT" -> "babel",
      "VERSION" -> version.value
    ),
    paradox := (Compile / paradox).dependsOn(mdoc.toTask("")).value
  )
  .dependsOn(core, loader, generic)

lazy val tests = module(identifier = Some("tests"), crossType = CrossType.Full)
  .settings(noPublishSettings)
  .settings(
    libraryDependencies ++=
      "org.scalameta" %%% "munit" % Version.Munit % "test" ::
        "org.typelevel" %%% "cats-laws" % Version.Cats % "test" ::
        "org.typelevel" %%% "discipline-munit" % Version.DisciplineMunit % "test" ::
        "org.typelevel" %%% "munit-cats-effect" % Version.MunitCatsEffect % "test" ::
        "org.scalameta" %%% "munit-scalacheck" % Version.MunitScalacheck % "test" ::
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
        "org.typelevel" %% "log4cats-slf4j" % Version.Log4Cats ::
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
