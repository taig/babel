lazy val root = project.in( file( "." ) )
    .settings( Settings.common ++ Settings.noPublish )
    .aggregate( lokalJVM, lokalJS )

lazy val lokal = crossProject.in( file( "." ) )
    .settings( Settings.common )
    .settings(
        description := "i18n & l10n for (isomorphic) Scala applications",
        libraryDependencies ++=
            "org.typelevel" %%% "cats-core" % "0.9.0" ::
            "org.scalatest" %%% "scalatest" % "3.0.3" % "test" ::
            Nil,
        name := "lokal",
        startYear := Some( 2017 )
    )

lazy val lokalJVM = lokal.jvm

lazy val lokalJS = lokal.js

lazy val documentation = project
    .enablePlugins( BuildInfoPlugin, MicrositesPlugin )
    .settings( Settings.common ++ Settings.noPublish )
    .settings(
        micrositeAuthor := "Niklas Klein",
        micrositeGithubOwner := "Taig",
        micrositeGithubRepo := "lokal",
        micrositeGithubToken := Option( System.getenv( "GITHUB_TOKEN" ) ),
        micrositeHighlightTheme := "atom-one-light",
        micrositeName := "Lokal",
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
        micrositeTwitterCreator := "@tttaig"
    )
    .dependsOn( lokalJVM )