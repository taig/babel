package io.taig.babel

object generic {
  object semiauto {
    def deriveEncoder[A](implicit encoder: DerivedEncoder[A]): Encoder[A] = encoder

    def deriveDecoder[A](implicit decoder: DerivedDecoder[A]): Decoder[A] = decoder
  }

  object auto {
    implicit def deriveEncoder[A](implicit encoder: DerivedEncoder[A]): Encoder[A] = encoder

    implicit def deriveDecoder[A](implicit decoder: DerivedDecoder[A]): Decoder[A] = decoder
  }
}
