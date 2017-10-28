import io.taig.sbt.sonatype.SonatypeHouserulesPlugin.autoImport._
import sbt._
import sbt.Keys._
import scoverage.ScoverageSbtPlugin.autoImport._

object Settings {
    val Scala212 = "2.12.4"

    val Scala211 = "2.11.11"

    val common = Def.settings(
        coverageHighlighting := {
            scalaBinaryVersion.value match {
                case "2.10" => false
                case _ => true
            }
        },
        crossScalaVersions :=
            Scala212 ::
            Scala211 ::
            Nil,
        githubProject := "lokal",
        homepage := Some( url( "http://taig.io/lokal/" ) ),
        organization := "io.taig",
        scalacOptions ++=
            "-deprecation" ::
            "-feature" ::
            Nil,
        scalaVersion := Scala212
    )

    val noPublish = Def.settings(
        publish := {},
        publishArtifact := false,
        publishLocal := {}
    )
}