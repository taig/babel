import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val Version = new {
  val CatsEffect = "2.3.1"
  val Circe = "0.13.0"
  val Classgraph = "4.8.98"
  val Fs2 = "2.5.0"
  val Http4s = "0.21.15"
  val Munit = "0.7.20"
  val MunitCatsEffect = "0.12.0"
  val Sconfig = "1.3.6"
  val Shapeless = "2.3.3"
  val Slf4j = "1.7.30"
}

noPublishSettings

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
      Seq(languages, countries, locales)
    }.taskValue
  )

lazy val formatterPrintf = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/formatter-printf"))
  .settings(
    name := "babel-formatter-printf"
  )
  .jsSettings(
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule))
  )
  .dependsOn(core % "compile->compile;test->test")

lazy val formatterMessageFormat = project
  .in(file("modules/formatter-message-format"))
  .settings(
    name := "babel-formatter-message-format"
  )
  .dependsOn(core.jvm % "compile->compile;test->test")

lazy val loader = project
  .in(file("modules/loader"))
  .settings(
    libraryDependencies ++=
      "io.github.classgraph" % "classgraph" % Version.Classgraph ::
        "co.fs2" %% "fs2-io" % Version.Fs2 ::
        "org.typelevel" %% "cats-effect" % Version.CatsEffect ::
        Nil,
    name := "babel-loader"
  )
  .dependsOn(core.jvm)

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

lazy val hocon = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/hocon"))
  .settings(
    libraryDependencies ++=
      "org.ekrich" %%% "sconfig" % Version.Sconfig ::
        Nil,
    name := "babel-hocon"
  )
  .dependsOn(circe)

lazy val tests = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/tests"))
  .settings(noPublishSettings)
  .settings(
    libraryDependencies ++=
      "org.scalameta" %%% "munit" % Version.Munit % "test" ::
        "org.typelevel" %%% "munit-cats-effect-2" % Version.MunitCatsEffect % "test" ::
        Nil,
    name := "babel-tests",
    testFrameworks += new TestFramework("munit.Framework")
  )
  .dependsOn(core, circe, hocon, generic, formatterPrintf)
  .jvmConfigure(_.dependsOn(loader, formatterMessageFormat))
  .jsSettings(
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule))
  )

lazy val sampleBackend = project
  .in(file("modules/sample/backend"))
  .settings(noPublishSettings)
  .settings(
    libraryDependencies ++=
      "org.http4s" %% "http4s-dsl" % Version.Http4s ::
        "org.http4s" %% "http4s-blaze-server" % Version.Http4s ::
        "org.slf4j" % "slf4j-simple" % Version.Slf4j ::
        Nil,
    name := "babel-sample-backend"
  )
  .dependsOn(formatterPrintf.jvm, generic.jvm, hocon.jvm, loader)
