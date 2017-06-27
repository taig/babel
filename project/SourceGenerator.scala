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
    
    def countriesTrait( pkg: String ): String =
        s"""
         |package $pkg
         |
         |trait Countries {
         |${countries.map( ( countryVal _ ).tupled ).mkString( "\n\n" )}
         |}
         """.stripMargin.trim
    
    def countryVal( code: String, name: String ): String =
        s"""|    // $name
            |    val $code = Country( "$code" )""".stripMargin
}