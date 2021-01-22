package net.slozzer.babel

import cats.effect._
import cats.effect.concurrent.{MVar, MVar2}
import cats.syntax.all._
import io.github.classgraph.ClassGraph

import java.net.URI
import java.nio.file.{FileSystem, FileSystemNotFoundException, FileSystems, Path => JPath}
import java.util.Collections
import scala.jdk.CollectionConverters._
import java.nio.file.Files

final class ClassgraphLoader[F[_]: Sync: ContextShift](fileSystems: MVar2[F, Set[FileSystem]])(blocker: Blocker)
    extends Loader[F] {
  override def scan(base: String): F[Set[JPath]] =
    blocker
      .delay(new ClassGraph().acceptPaths(base).scan())
      .flatMap(_.getAllResources.asScala.toList.traverse(resource => createPath(resource.getURI)))
      .map(_.toSet)

  override def filter(paths: Set[JPath], name: String): F[Map[Option[Locale], JPath]] =
    paths.toList
      .filterA(path => blocker.delay(Files.isRegularFile(path)))
      .map(_.map(path => locale(path).tupleRight(path)).collect { case Right(result) => result }.toMap)

  def createPath(uri: URI): F[JPath] =
    if (uri.getScheme === "file") blocker.delay(JPath.of(uri))
    else
      fileSystems.modify { fileSystems =>
        blocker
          .delay(FileSystems.getFileSystem(uri))
          .as(fileSystems)
          .recoverWith { case _: FileSystemNotFoundException =>
            blocker.delay(fileSystems + FileSystems.newFileSystem(uri, Collections.emptyMap[String, Any]()))
          }
          .flatMap(blocker.delay(JPath.of(uri)).tupleLeft)
      }

  def locale(path: JPath): Either[String, Option[Locale]] =
    Option(path.getFileName())
      .toRight("Invalid path")
      .map(_.toString)
      .map { fileName =>
        fileName.indexOf('.') match {
          case -1    => fileName
          case index => fileName.substring(0, index)
        }
      }
      .flatMap {
        case Loader.Wildcard => none[Locale].asRight
        case name            => Locale.parseLanguageTag(name).toRight(s"Invalid language tag: $name").map(_.some)

      }
}

object ClassgraphLoader {
  def apply[F[_]: Concurrent: ContextShift](blocker: Blocker): Resource[F, Loader[F]] =
    Resource
      .make(MVar.of[F, Set[FileSystem]](Set.empty)) { fileSystems =>
        fileSystems.use { fileSystems =>
          fileSystems.toList.traverse_(fileSystem => blocker.delay(fileSystem.close()))
        }
      }
      .map(new ClassgraphLoader[F](_)(blocker))
}
