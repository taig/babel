import java.util.Locale

object SourceGenerator {
  val locales: Array[Locale] = Locale.getAvailableLocales
    .filter { locale =>
      val label = locale.toString
      label.nonEmpty && !label.contains("#") && label.length <= 5
    }

  def identifier(value: String): String = value.toLowerCase.capitalize

  def languages(pkg: String): String = {
    val languages = locales.map(_.getLanguage).toSet
    val vals = languages.map { language =>
      s"""val ${identifier(language)}: Language = Language("$language")"""
    }

    s"""package $pkg.dsl
       |
       |import $pkg.Language
       |
       |object Languages {
       |  ${vals.mkString("\n\n  ")}
       |
       |  val All: List[Language] = List(${languages
         .map(identifier)
         .mkString(", ")})
       |}""".stripMargin
  }

  def countries(pkg: String): String = {
    val countries = locales.map(_.getCountry).filter(_.nonEmpty).toSet
    val vals = countries.map { country =>
      s"""val ${identifier(country)}: Country = Country("$country")"""
    }

    s"""package $pkg.dsl
       |
       |import $pkg.Country
       |
       |object Countries {
       |  ${vals.mkString("\n\n  ")}
       |
       |  val All: List[Country] = List(${countries
         .map(identifier)
         .mkString(", ")})
       |}""".stripMargin
  }

  def locales(pkg: String): String = {
    val name: Locale => String = locale =>
      if (locale.getCountry.isEmpty) locale.getLanguage
      else s"${locale.getLanguage}_${locale.getCountry}"

    val vals = locales.map { locale =>
      val language = locale.getLanguage
      val country = locale.getCountry

      if (country.isEmpty) {
        s"val ${name(locale)}: Locale = Locale(Languages.${identifier(language)})"
      } else {
        s"val ${name(locale)}: Locale = Locale(" +
          s"Languages.${identifier(language)}, " +
          s"Some(Countries.${identifier(country)}))"
      }
    }

    s"""package $pkg.dsl
       |
       |import $pkg.Locale
       |import $pkg.dsl.Languages
       |import $pkg.dsl.Countries
       |
       |object Locales {
       |  ${vals.mkString("\n\n  ")}
       |
       |  val All: List[Locale] = List(${locales.map(name).mkString(", ")})
       |}""".stripMargin
  }

//  val allVal: String =
//    s"  val All: List[Locale] = List(${locales.map(_.toString).mkString(", ")})"
//
//  def stringOperationsTrait: String =
//    s"""trait LokalStringOperations { this: LokalStringContext =>
//       |${locales.map(stringOperationDef).mkString("\n\n")}
//       |}""".stripMargin
//
//  def stringOperationDef(locale: Locale): String = {
//    val identifier = locale.toString
//
//    s"""  @inline
//       |  def $identifier(arguments: Any*): Translation[String] =
//       |    apply(Locale.$identifier, arguments)""".stripMargin
//  }
}
