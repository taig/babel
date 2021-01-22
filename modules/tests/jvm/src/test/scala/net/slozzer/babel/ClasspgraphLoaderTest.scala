package net.slozzer.babel
import cats.effect.{Blocker, IO, Resource}

final class ClasspgraphLoaderTest extends LoaderTest {
  override val loader: Resource[IO, Loader[IO]] = Blocker[IO].flatMap(ClassgraphLoader[IO])
}
