package net.slozzer.babel

import java.net.URI
import java.nio.file.{FileSystem, FileSystemNotFoundException, FileSystems, Path => JPath}
import java.util.Collections

import cats.effect._
import cats.effect.concurrent.{MVar, MVar2}
import cats.syntax.all._
import fs2.io.file.readAll
import fs2.text.utf8Decode
import io.github.classgraph.ClassGraph

import scala.jdk.CollectionConverters._

final class ClassgraphLoader[F[_]: Sync: ContextShift](fileSystems: MVar2[F, Set[FileSystem]])(blocker: Blocker)
    extends Loader[F] {
  override def scan(base: String): F[Set[JPath]] =
    blocker
      .delay(new ClassGraph().acceptPaths(base).scan())
      .flatMap(_.getAllResources.asScala.toList.traverse(resource => createPath(resource.getURI)))
      .map(_.toSet)

  override def load(translations: Translations[JPath]): F[Translations[Array[Byte]]] =
    translations.values.toList
      .traverse { case (locale, path) =>
        readAll(path, blocker, chunkSize = 8192).compile.to(Array).tupleLeft(locale)
      }
      .map(Translations.from(_))

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
