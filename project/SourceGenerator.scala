import java.util.Locale

import sbt._
import sbt.Def.Initialize
import sbt.Keys._

object SourceGenerator {
  val locales = Locale.getAvailableLocales

  val countries =
    locales
      .map { locale =>
        val code = locale.getCountry
        val name = locale.getDisplayCountry(Locale.ENGLISH)
        (code, name)
      }
      .filter { case (code, name) => code != "" && name != "" }
      .distinct
      .sorted

  val languages =
    locales
      .map { locale =>
        val code = locale.getLanguage
        val name = locale.getDisplayLanguage(Locale.ENGLISH)
        (code, name)
      }
      .filter { case (code, name) => code != "" && name != "" }
      .distinct
      .sorted

  val identifiers =
    locales
      .map(_.toString)
      .filter(locale ⇒ locale.length == 2 || locale.length == 5)
      .sorted
      .map(_.split("_"))
      .map {
        case Array(language) ⇒ (language, None)
        case Array(language, country) ⇒
          (language, Some(country))
      }

  val countriesTrait: String =
    s"""|trait Countries {
            |${countries.map((countryVal _).tupled).mkString("\n\n")}
            |}""".stripMargin

  def countryVal(code: String, name: String): String =
    s"""|    /**
            |     * $name
            |     */
            |    val $code = Country( "$code" )""".stripMargin

  val languagesTrait: String =
    s"""|trait Languages {
            |${languages.map((languageVal _).tupled).mkString("\n\n")}
            |}""".stripMargin

  def languageVal(code: String, name: String): String =
    s"""|    /**
            |     * $name
            |     */
            |    val $code = Language( "$code" )""".stripMargin

  val identifiersTrait: String =
    s"""|trait LocalizationIdentifiers {
            |${identifiers.map((identifierVal _).tupled).mkString("\n\n")}
            |}""".stripMargin

  def identifierVal(
      language: String,
      countryOption: Option[String]
  ): String = {
    val (name, country) = countryOption match {
      case Some(country) =>
        (s"${language}_$country", s"Some( Country.$country )")
      case None => (language, "None")
    }

    s"    val $name = LocalizationIdentifier( Language.$language, $country )"
  }

  def stringOperationsTrait: String =
    s"""|trait StringOperations { this: operation.string =>
            |${identifiers.map((stringOperationDef _).tupled).mkString("\n\n")}
            |}""".stripMargin

  def stringOperationDef(
      language: String,
      countryOption: Option[String]
  ): String = {
    val identifier = countryOption.fold(language) { country =>
      s"${language}_$country"
    }

    s"""|    def $identifier( arguments: Any* ): Localization[String] =
            |         Localization(
            |             LocalizationIdentifier.$identifier,
            |             substitude( LocalizationIdentifier.$identifier, arguments )
            |         )""".stripMargin
  }
}
