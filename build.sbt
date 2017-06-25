lazy val root = project.in( file( "." ) )
    .settings( Settings.common ++ Settings.noPublish )
    .aggregate( lokalJVM, lokalJS )

lazy val lokal = crossProject.in( file( "." ) )
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            "org.typelevel" %%% "cats-core" % "0.9.0" ::
            "org.scalatest" %%% "scalatest" % "3.0.3" % "test" ::
            Nil,
        name := "lokal"
    )

lazy val lokalJVM = lokal.jvm

lazy val lokalJS = lokal.js

lazy val documentation = project
    .enablePlugins( MicrositesPlugin )
    .settings( Settings.common ++ Settings.noPublish )
    .settings(
        micrositeName := "Lokal",
        micrositeDescription := "i18n & l10n for (isomorphic) Scala applications",
        micrositeAuthor := "Niklas Klein",
        micrositeGithubOwner := "Taig",
        micrositeGithubRepo := "lokal",
        micrositeTwitterCreator := "@tttaig",
        micrositeHighlightTheme := "atom-one-light",
        micrositePalette := Map(
            "brand-primary" -> "#3e4959",
            "brand-secondary" -> "#3e4959",
            "brand-tertiary" -> "#3e4959",
            "gray-dark" -> "#3e4959",
            "gray" -> "#837f84",
            "gray-light" -> "#e3e2e3",
            "gray-lighter" -> "#f4f3f4",
            "white-color" -> "#f3f3f3"
        )
    )
    .dependsOn( lokalJVM )