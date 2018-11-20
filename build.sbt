import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

lazy val lokal = project
  .in(file("."))
  .settings(Settings.common ++ Settings.noPublish)
  .aggregate(coreJVM, coreJS)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .withoutSuffixFor(JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(Settings.common)
  .settings(
    description := "i18n & l10n for (isomorphic) Scala applications",
    libraryDependencies ++=
      "org.typelevel" %%% "cats-core" % "1.4.0" ::
        "io.github.cquiroz" %%% "scala-java-locales" % "0.5.5-cldr31" ::
        "org.scalatest" %%% "scalatest" % "3.0.5" % "test" ::
        "org.typelevel" %%% "cats-testkit" % "1.4.0" % "test" ::
        Nil,
    name := "Lokal",
    sourceGenerators in Compile += Def.task {
      val source =
        SourceGenerator.render(s"${organization.value}.${normalizedName.value}")
      val file = (sourceManaged in Compile).value / "Definitions.scala"
      IO.write(file, source)
      Seq(file)
    }.taskValue,
    startYear := Some(2017)
  )

lazy val coreJVM = core.jvm

lazy val coreJS = core.js

lazy val documentation = project
  .enablePlugins(BuildInfoPlugin, MicrositesPlugin)
  .settings(Settings.common ++ Settings.noPublish)
  .settings(
    micrositeAnalyticsToken := "UA-64109905-2",
    micrositeAuthor := "Niklas Klein",
    micrositeBaseUrl := s"/${githubProject.value}",
    micrositeDescription := (description in coreJVM).value,
    micrositeDocumentationUrl := {
      val o = organization.value
      val n = (normalizedName in coreJVM).value
      val a = s"${n}_${scalaBinaryVersion.value}"
      val v = version.value
      val p = s"$o.$n".split("\\.").mkString("/")
      s"https://static.javadoc.io/$o/$a/$v/$p/index.html"
    },
    micrositeGithubOwner := "Taig",
    micrositeGithubRepo := githubProject.value,
    micrositeGithubToken := Option(System.getenv("GITHUB_TOKEN")),
    micrositeGitterChannel := false,
    micrositeHighlightTheme := "atom-one-dark",
    micrositeName := (name in coreJVM).value,
    micrositePalette := Map(
      "brand-primary" -> "#3e4959",
      "brand-secondary" -> "#3e4959",
      "brand-tertiary" -> "#3e4959",
      "gray-dark" -> "#3e4959",
      "gray" -> "#837f84",
      "gray-light" -> "#e3e2e3",
      "gray-lighter" -> "#f4f3f4",
      "white-color" -> "#f3f3f3"
    ),
    micrositePushSiteWith := GitHub4s,
    micrositeTwitterCreator := "@tttaig",
    scalacOptions in Tut -= "-Ywarn-unused-import",
    tutSourceDirectory := sourceDirectory.value
  )
  .settings(
    buildInfoObject := "Build",
    buildInfoPackage := s"${organization.value}.${(normalizedName in coreJVM).value}",
    buildInfoKeys := Seq[BuildInfoKey](
      normalizedName in coreJVM,
      organization,
      version
    )
  )
  .dependsOn(coreJVM)

addCommandAlias("scalafmtAll", ";scalafmt;test:scalafmt;scalafmtSbt")
addCommandAlias("scalafmtTestAll",
                ";scalafmtCheck;test:scalafmtCheck;scalafmtSbtCheck")
