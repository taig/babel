import sbt.Def
import sbt.Keys._

object Settings {
    val Scala212 = "2.12.2"
    
    val Scala211 = "2.11.11"
    
    val Scala210 = "2.10.6"
    
    val common = Def.settings(
        crossScalaVersions :=
            Scala212 ::
            Scala211 ::
            Scala210 ::
            Nil,
        organization := "io.taig",
        scalaVersion := Scala212
    )
    
    val noPublish = Def.settings(
        publish := {},
        publishArtifact := false,
        publishLocal := {}
    )
}