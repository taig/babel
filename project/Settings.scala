import sbt._
import sbt.Keys._

object Settings {
  val common: Seq[Def.Setting[_]] = Def.settings(
    homepage := Some(url("https://taig.io/lokal/")),
    startYear := Some(2017)
  )
}
