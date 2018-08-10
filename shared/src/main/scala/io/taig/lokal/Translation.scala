package io.taig.lokal

import java.util.Locale

case class Translation[A](locale: Locale, value: A)
