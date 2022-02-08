package io.taig.babel

import cats.effect.kernel.Sync
import cats.syntax.all._
import org.ekrich.config.{Config, ConfigFactory}

import scala.jdk.CollectionConverters._

final class HoconLoader[F[_]](implicit F: Sync[F]) extends Loader[F] {
  override def load(base: String, locales: Set[Locale]): F[Translations[Babel]] =
    locales.toList
      .traverse { locale =>
        val resource = if (base.isEmpty) locale.printLanguageTag else s"$base/${locale.printLanguageTag}"
        F.blocking(ConfigFactory.parseResourcesAnySyntax(resource)).tupleLeft(locale)
      }
      .map(configs => configs.traverse { case (locale, config) => toBabel(config).map(Translation(locale, _)) })
      .rethrow
      .map(Translations.from)

  def toBabel(config: Config): Either[Throwable, Babel] =
    config.root.entrySet.asScala
      .map(entry => entry.getKey -> entry.getValue)
      .toList
      .foldLeftM(Map.empty[String, Babel]) { case (result, (key, value)) =>
        toBabel(value.unwrapped, Path.one(key)).map(babel => result + (key -> babel))
      }
      .map(Babel.Object.apply)

  def toBabel(value: AnyRef, path: Path): Either[Throwable, Babel] = value match {
    case value: String => Babel.Value(value).asRight
    case obj: java.util.Map[_, _] =>
      obj.asScala.toList
        .traverse { case (a, b) =>
          val key = a.asInstanceOf[String]
          val value = b.asInstanceOf[AnyRef]
          toBabel(value, path / key).tupleLeft(key)
        }
        .map(Babel.from)
    case null => Right(Babel.Null)
    case value =>
      val message = s"Unsupported type: ${value.getClass.getSimpleName} ${path.printPlaceholder}"
      Left(new IllegalArgumentException(message))
  }
}

object HoconLoader {
  def apply[F[_]: Sync]: Loader[F] = new HoconLoader[F]
}
