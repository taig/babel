import java.util.Locale

import sbt._
import sbt.Def.Initialize
import sbt.Keys._

object SourceGenerator {
  val locales: Array[Locale] = Locale.getAvailableLocales
    .filter { locale =>
      val label = locale.toString
      label.nonEmpty && !label.contains("#") && label.length <= 5
    }

  def render(pkg: String): String =
    s"""package $pkg
       |
       |import java.util.Locale
       |import io.taig.lokal.syntax.LokalStringContext
       |
       |$localesObject
       |
       |$stringOperationsTrait""".stripMargin

  def localesObject: String =
    s"""object Locales {
       |  ${locales.map(localeVal).mkString("\n\n  ")}
       |}""".stripMargin

  def localeVal(locale: Locale): String =
    s"""val ${locale.toString} = new Locale("${locale.getLanguage}", "${locale.getCountry}")"""

  def stringOperationsTrait: String =
    s"""trait LokalStringOperations { this: LokalStringContext =>
       |  ${locales.map(stringOperationDef).mkString("\n\n  ")}
       |}""".stripMargin

  def stringOperationDef(locale: Locale): String = {
    val identifier = locale.toString

    s"""def $identifier(arguments: Any*): Translations[String] =
       |    apply(Locales.$identifier, arguments)""".stripMargin
  }
}
