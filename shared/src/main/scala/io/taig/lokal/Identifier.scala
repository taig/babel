package io.taig.lokal

case class Identifier( language: Language, country: Option[Country] ) {
    def compare( identifier: Identifier ): Identifier.Comparison =
        if ( this == identifier ) Identifier.Comparison.Exact
        else if ( this.language == identifier.language )
            if ( this.country.isDefined && identifier.country.isEmpty )
                Identifier.Comparison.Almost
            else Identifier.Comparison.Weak
        else Identifier.Comparison.None

    override def toString: String = country.fold( language.value ) { country ⇒
        s"${language.value}-${country.value}"
    }
}

object Identifier extends Identifiers {
    sealed trait Comparison extends Product with Serializable

    object Comparison {
        case object Exact extends Comparison
        case object Almost extends Comparison
        case object Weak extends Comparison
        case object None extends Comparison
    }

    def parse( identifier: String ): Option[Identifier] =
        identifier.split( "-" ) match {
            case Array( language ) if language.length == 2 ⇒
                Some( Identifier( Language( language ), None ) )
            case Array( language, country ) if language.length == 2 && country.length == 2 ⇒
                val identifier = Identifier(
                    Language( language ),
                    Some( Country( country ) )
                )
                Some( identifier )
            case _ ⇒ None
        }
}