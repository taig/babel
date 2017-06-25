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

lazy val documentation = project.in( file( ".documentation/" ) )
    .enablePlugins( TutPlugin )
    .settings( Settings.common ++ Settings.noPublish )
    .settings(
        tutSourceDirectory := ( baseDirectory in ThisBuild ).value / "tut"
    )
    .dependsOn( lokalJVM )