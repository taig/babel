import java.util.Locale

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
       |${locales.map(localeVal).mkString("\n\n")}
       |
       |$allVal
       |}""".stripMargin

  def localeVal(locale: Locale): String =
    s"""  val ${locale.toString} = new Locale("${locale.getLanguage}", "${locale.getCountry}")"""

  val allVal: String =
    s"  val All: List[Locale] = List(${locales.map(_.toString).mkString(", ")})"

  def stringOperationsTrait: String =
    s"""trait LokalStringOperations { this: LokalStringContext =>
       |${locales.map(stringOperationDef).mkString("\n\n  ")}
       |}""".stripMargin

  def stringOperationDef(locale: Locale): String = {
    val identifier = locale.toString

    s"""  @inline
       |  def $identifier(arguments: Any*): Translation[Option[String]] =
       |    apply(Locales.$identifier, arguments)""".stripMargin
  }
}
