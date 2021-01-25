package net.slozzer.babel

object generic {
  object semiauto {
    def deriveEncoder[F[_], A](implicit encoder: DerivedEncoder[F[A], A]): Encoder[F, A] = new Encoder[F, A] {
      override def encode(value: F[A]): Segments[A] = encoder.encode(value)
    }

    def deriveDecoder[A](implicit decoder: DerivedDecoder[A]): Decoder[A] = new Decoder[A] {
      override def decode(babel: Babel, path: Path): Either[Decoder.Error, A] = decoder.decode(babel, path)
    }
  }

  object auto {
    implicit def derivedEncoder[F[_], A](implicit encoder: DerivedEncoder[F[A], A]): Encoder[F, A] =
      semiauto.deriveEncoder(encoder)

    implicit def derivedDecoder[A](implicit decoder: DerivedDecoder[A]): Decoder[A] = semiauto.deriveDecoder(decoder)
  }
}
