package net.slozzer.babel

object generic {
  object semiauto {
    def deriveEncoder[A](implicit encoder: DerivedEncoder[A]): Encoder[A] = encoder.encode

    def deriveDecoder[A](implicit decoder: SemiautoDecoder[A]): Decoder[A] = decoder.decode
  }

  object auto {
    def derivedEncoder[A](implicit encoder: DerivedEncoder[A]): Encoder[A] = semiauto.deriveEncoder(encoder)

    def derivedDecoder[A](implicit decoder: SemiautoDecoder[A]): Decoder[A] = semiauto.deriveDecoder(decoder)
  }
}
