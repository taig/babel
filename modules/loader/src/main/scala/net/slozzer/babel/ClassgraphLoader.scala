package net.slozzer.babel

import cats.effect._
import cats.effect.concurrent.{MVar, MVar2}
import cats.syntax.all._
import io.github.classgraph.ClassGraph

import java.net.URI
import java.nio.file.{FileSystem, FileSystemNotFoundException, FileSystems, Path => JPath}
import java.util.Collections
import scala.jdk.CollectionConverters._

final class ClassgraphLoader[F[_]: Sync: ContextShift](fileSystems: MVar2[F, Set[FileSystem]])(blocker: Blocker)
    extends Loader[F] {
  override def scan(name: String): F[Map[Option[Locale], JPath]] = {
    val (base, extension) = name.indexOf('.') match {
      case -1    => (name, None)
      case index => (name.substring(0, index), Some(name.substring(index)))
    }

    blocker
      .delay(new ClassGraph().acceptPathsNonRecursive(base).scan())
      .flatMap(_.getAllResources.asScala.toList.traverse(resource => createPath(resource.getURI)))
      .map(paths => extension.fold(paths)(extension => paths.filter(_.toString.endsWith(extension))))
      .flatMap(_.traverse(path => locale(path).tupleRight(path)))
      .map(_.toMap)
  }

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

  def locale(path: JPath): F[Option[Locale]] = {
    Option(path.getFileName())
      .liftTo[F](new IllegalArgumentException(s"Invalid file $path"))
      .map(_.toString)
      .flatMap { fileName =>
        val name = fileName.indexOf('.') match {
          case -1    => fileName
          case index => fileName.substring(0, index)
        }

        name match {
          case "*" => none[Locale].pure[F]
          case tag =>
            Locale
              .parseLanguageTag(tag)
              .liftTo[F](new IllegalArgumentException("Not a a valid language tag"))
              .map(_.some)
        }
      }
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
