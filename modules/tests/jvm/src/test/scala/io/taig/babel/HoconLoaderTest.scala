package io.taig.babel

import _root_.cats.effect.{IO, Resource}

final class HoconLoaderTest extends LoaderTest {
  override val loader: Resource[IO, Loader[IO]] = Resource.pure(HoconLoader[IO])
}
