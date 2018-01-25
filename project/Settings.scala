import io.taig.sbt.sonatype.SonatypeHouserulesPlugin.autoImport._
import sbt._
import sbt.Keys._
import scoverage.ScoverageSbtPlugin.autoImport._

object Settings {
    val Scala212 = "2.12.4"

    val Scala211 = "2.11.12"

    val common = Def.settings(
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
            "-Xfatal-warnings" ::
            "-Ywarn-unused-import" ::
            Nil,
        scalaVersion := Scala212
    )

    val noPublish = Def.settings(
        publish := {},
        publishArtifact := false,
        publishLocal := {}
    )
}