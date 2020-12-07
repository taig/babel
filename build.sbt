import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val Version = new {
  val Cats = "2.3.0"
  val CatsTestkitScalatest = "2.1.0"
  val Circe = "0.13.0"
  val ScalacheckShapeless = "1.2.5"
  val Shapeless = "2.3.3"
}

noPublishSettings

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "org.typelevel" %%% "cats-core" % Version.Cats ::
        "com.github.alexarchambault" %%% "scalacheck-shapeless_1.14" % Version.ScalacheckShapeless % "test" ::
        "org.typelevel" %%% "cats-testkit-scalatest" % Version.CatsTestkitScalatest % "test" ::
        Nil,
    name := "lokal-core"
  )

lazy val generic = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "com.chuusai" %%% "shapeless" % Version.Shapeless ::
        Nil,
    name := "lokal-generic"
  )
  .dependsOn(core)

lazy val circe = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "com.chuusai" %%% "shapeless" % Version.Shapeless ::
        "io.circe" %%% "circe-parser" % Version.Circe ::
        Nil,
    name := "lokal-circe"
  )
  .dependsOn(core)

lazy val dsl = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(sonatypePublishSettings)
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

lazy val sample = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(noPublishSettings)
  .settings(
    name := "lokal-sample",
    libraryDependencies ++=
      "io.circe" %%% "circe-generic" % Version.Circe ::
        Nil
  )
  .dependsOn(circe, dsl, generic)
