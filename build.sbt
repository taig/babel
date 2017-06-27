lazy val lokal = project.in( file( "." ) )
    .settings( Settings.common ++ Settings.noPublish )
    .aggregate( coreJVM, coreJS )

lazy val core = crossProject.in( file( "." ) )
    .settings( Settings.common )
    .settings(
        description := "i18n & l10n for (isomorphic) Scala applications",
        libraryDependencies ++=
            "org.typelevel" %%% "cats-core" % "0.9.0" ::
            "org.scalatest" %%% "scalatest" % "3.0.3" % "test" ::
            Nil,
        name := "Lokal",
        sourceGenerators in Compile += Def.task {
            val source =
                s"""|package ${organization.value}.${normalizedName.value}
                    |
                    |${SourceGenerator.countriesTrait}
                    |
                    |${SourceGenerator.languagesTrait}
                    |
                    |${SourceGenerator.identifiersTrait}
                 """.stripMargin

            val file = ( sourceManaged in Compile ).value / "Definitions.scala"
            IO.write( file, source )
            Seq( file )
        }.taskValue,
        startYear := Some( 2017 )
    )

lazy val coreJVM = core.jvm

lazy val coreJS = core.js

lazy val documentation = project
    .enablePlugins( BuildInfoPlugin, MicrositesPlugin )
    .settings( Settings.common ++ Settings.noPublish )
    .settings(
        micrositeAnalyticsToken := "UA-64109905-2",
        micrositeAuthor := "Niklas Klein",
        micrositeBaseUrl := s"/${githubProject.value}",
        micrositeCssDirectory := sourceDirectory.value / "stylesheet",
        micrositeDescription := ( description in coreJVM ).value,
        micrositeGithubOwner := "Taig",
        micrositeGithubRepo := githubProject.value,
        micrositeGithubToken := Option( System.getenv( "GITHUB_TOKEN" ) ),
        micrositeGitterChannel := false,
        micrositeHighlightTheme := "atom-one-dark",
        micrositeName := ( name in coreJVM ).value,
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
        micrositeTwitterCreator := "@tttaig",
        tutSourceDirectory := sourceDirectory.value
    )
    .settings(
        buildInfoObject := "Build",
        buildInfoPackage := s"${organization.value}.${( normalizedName in coreJVM ).value}",
        buildInfoKeys := Seq[BuildInfoKey](
            crossScalaVersions,
            normalizedName in coreJVM,
            organization,
            version
        )
    )
    .dependsOn( coreJVM )