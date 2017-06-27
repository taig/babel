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
                val name = locale.getDisplayCountry( Locale.ENGLISH )
                ( code, name )
            }
            .filter { case ( code, name ) => code != "" && name != "" }
            .distinct
            .sorted
    
    val languages =
        locales
            .map { locale =>
                val code = locale.getLanguage
                val name = locale.getDisplayLanguage( Locale.ENGLISH )
                ( code, name )
            }
            .filter { case ( code, name ) => code != "" && name != "" }
            .distinct
            .sorted
    
    val countriesTrait: String =
        s"""|trait Countries {
            |${countries.map( ( countryVal _ ).tupled ).mkString( "\n\n" )}
            |}""".stripMargin
    
    def countryVal( code: String, name: String ): String =
        s"""|    // $name
            |    val $code = Country( "$code" )""".stripMargin

    val languagesTrait: String =
        s"""|trait Languages {
            |${languages.map( ( languageVal _ ).tupled ).mkString( "\n\n" )}
            |}""".stripMargin

    def languageVal( code: String, name: String ): String =
        s"""|    // $name
            |    val $code = Language( "$code" )""".stripMargin
}