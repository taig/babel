package net.slozzer.babel
import cats.effect.{Blocker, IO, Resource}

final class HoconLoaderTest extends LoaderTest {
  override val loader: Resource[IO, Loader[IO]] = Blocker[IO].map(HoconLoader[IO])
}
