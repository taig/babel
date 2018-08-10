package io.taig.lokal.operation

import io.taig.lokal._

final class string(val context: StringContext) extends StringOperations {
  protected def substitude(
      identifier: LocalizationIdentifier,
      arguments: Seq[Any]
  ): String = {
    val translated = arguments.map {
      case translation @ Translation(_) ⇒
        translation.translate(identifier)
      case Localization(_, value) ⇒ value
      case default ⇒ default
    }

    context.s(translated: _*)
  }
}
