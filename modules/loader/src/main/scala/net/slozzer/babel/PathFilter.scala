package net.slozzer.babel

import java.nio.file.{Path => JPath}

final case class PathFilter(matches: JPath => Boolean) extends AnyVal {
  def &&(filter: PathFilter): PathFilter = PathFilter(path => matches(path) && filter.matches(path))

  def ||(filter: PathFilter): PathFilter = PathFilter(path => matches(path) || filter.matches(path))
}

object PathFilter {
  val valid: PathFilter = PathFilter(_ => true)

  def extension(name: String): PathFilter = PathFilter(_.toString.endsWith(s".$name"))

  def parent(name: String): PathFilter = PathFilter(path => Option(path.getParent).exists(_.endsWith(name)))
}
