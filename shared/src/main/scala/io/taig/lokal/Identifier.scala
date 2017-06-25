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

/**
 * Predefined language country identifier, taken from the JDK Locale definitions
 *
 * {{{
 * Locale
 *     .getAvailableLocales
 *     .map( _.toString )
 *     .filter( locale ⇒ locale.length == 2 || locale.length == 5 )
 *     .sorted
 *     .map( _.split( "_" ) )
 *     .map {
 *         case Array( language ) ⇒
 *             s"val $language = Identifier( Language.$language, None )"
 *         case Array( language, country ) ⇒
 *             s"val ${language}_$country = Identifier( Language.$language, Some( Country.$country ) )"
 *     }
 *     .foreach( println )
 * }}}
 */
object Identifier {
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

    val ar = Identifier( Language.ar, None )
    val ar_AE = Identifier( Language.ar, Some( Country.AE ) )
    val ar_BH = Identifier( Language.ar, Some( Country.BH ) )
    val ar_DZ = Identifier( Language.ar, Some( Country.DZ ) )
    val ar_EG = Identifier( Language.ar, Some( Country.EG ) )
    val ar_IQ = Identifier( Language.ar, Some( Country.IQ ) )
    val ar_JO = Identifier( Language.ar, Some( Country.JO ) )
    val ar_KW = Identifier( Language.ar, Some( Country.KW ) )
    val ar_LB = Identifier( Language.ar, Some( Country.LB ) )
    val ar_LY = Identifier( Language.ar, Some( Country.LY ) )
    val ar_MA = Identifier( Language.ar, Some( Country.MA ) )
    val ar_OM = Identifier( Language.ar, Some( Country.OM ) )
    val ar_QA = Identifier( Language.ar, Some( Country.QA ) )
    val ar_SA = Identifier( Language.ar, Some( Country.SA ) )
    val ar_SD = Identifier( Language.ar, Some( Country.SD ) )
    val ar_SY = Identifier( Language.ar, Some( Country.SY ) )
    val ar_TN = Identifier( Language.ar, Some( Country.TN ) )
    val ar_YE = Identifier( Language.ar, Some( Country.YE ) )
    val be = Identifier( Language.be, None )
    val be_BY = Identifier( Language.be, Some( Country.BY ) )
    val bg = Identifier( Language.bg, None )
    val bg_BG = Identifier( Language.bg, Some( Country.BG ) )
    val ca = Identifier( Language.ca, None )
    val ca_ES = Identifier( Language.ca, Some( Country.ES ) )
    val cs = Identifier( Language.cs, None )
    val cs_CZ = Identifier( Language.cs, Some( Country.CZ ) )
    val da = Identifier( Language.da, None )
    val da_DK = Identifier( Language.da, Some( Country.DK ) )
    val de = Identifier( Language.de, None )
    val de_AT = Identifier( Language.de, Some( Country.AT ) )
    val de_CH = Identifier( Language.de, Some( Country.CH ) )
    val de_DE = Identifier( Language.de, Some( Country.DE ) )
    val de_GR = Identifier( Language.de, Some( Country.GR ) )
    val de_LU = Identifier( Language.de, Some( Country.LU ) )
    val el = Identifier( Language.el, None )
    val el_CY = Identifier( Language.el, Some( Country.CY ) )
    val el_GR = Identifier( Language.el, Some( Country.GR ) )
    val en = Identifier( Language.en, None )
    val en_AU = Identifier( Language.en, Some( Country.AU ) )
    val en_CA = Identifier( Language.en, Some( Country.CA ) )
    val en_GB = Identifier( Language.en, Some( Country.GB ) )
    val en_IE = Identifier( Language.en, Some( Country.IE ) )
    val en_IN = Identifier( Language.en, Some( Country.IN ) )
    val en_MT = Identifier( Language.en, Some( Country.MT ) )
    val en_NZ = Identifier( Language.en, Some( Country.NZ ) )
    val en_PH = Identifier( Language.en, Some( Country.PH ) )
    val en_SG = Identifier( Language.en, Some( Country.SG ) )
    val en_US = Identifier( Language.en, Some( Country.US ) )
    val en_ZA = Identifier( Language.en, Some( Country.ZA ) )
    val es = Identifier( Language.es, None )
    val es_AR = Identifier( Language.es, Some( Country.AR ) )
    val es_BO = Identifier( Language.es, Some( Country.BO ) )
    val es_CL = Identifier( Language.es, Some( Country.CL ) )
    val es_CO = Identifier( Language.es, Some( Country.CO ) )
    val es_CR = Identifier( Language.es, Some( Country.CR ) )
    val es_CU = Identifier( Language.es, Some( Country.CU ) )
    val es_DO = Identifier( Language.es, Some( Country.DO ) )
    val es_EC = Identifier( Language.es, Some( Country.EC ) )
    val es_ES = Identifier( Language.es, Some( Country.ES ) )
    val es_GT = Identifier( Language.es, Some( Country.GT ) )
    val es_HN = Identifier( Language.es, Some( Country.HN ) )
    val es_MX = Identifier( Language.es, Some( Country.MX ) )
    val es_NI = Identifier( Language.es, Some( Country.NI ) )
    val es_PA = Identifier( Language.es, Some( Country.PA ) )
    val es_PE = Identifier( Language.es, Some( Country.PE ) )
    val es_PR = Identifier( Language.es, Some( Country.PR ) )
    val es_PY = Identifier( Language.es, Some( Country.PY ) )
    val es_SV = Identifier( Language.es, Some( Country.SV ) )
    val es_US = Identifier( Language.es, Some( Country.US ) )
    val es_UY = Identifier( Language.es, Some( Country.UY ) )
    val es_VE = Identifier( Language.es, Some( Country.VE ) )
    val et = Identifier( Language.et, None )
    val et_EE = Identifier( Language.et, Some( Country.EE ) )
    val fi = Identifier( Language.fi, None )
    val fi_FI = Identifier( Language.fi, Some( Country.FI ) )
    val fr = Identifier( Language.fr, None )
    val fr_BE = Identifier( Language.fr, Some( Country.BE ) )
    val fr_CA = Identifier( Language.fr, Some( Country.CA ) )
    val fr_CH = Identifier( Language.fr, Some( Country.CH ) )
    val fr_FR = Identifier( Language.fr, Some( Country.FR ) )
    val fr_LU = Identifier( Language.fr, Some( Country.LU ) )
    val ga = Identifier( Language.ga, None )
    val ga_IE = Identifier( Language.ga, Some( Country.IE ) )
    val hi = Identifier( Language.hi, None )
    val hi_IN = Identifier( Language.hi, Some( Country.IN ) )
    val hr = Identifier( Language.hr, None )
    val hr_HR = Identifier( Language.hr, Some( Country.HR ) )
    val hu = Identifier( Language.hu, None )
    val hu_HU = Identifier( Language.hu, Some( Country.HU ) )
    val is = Identifier( Language.is, None )
    val is_IS = Identifier( Language.is, Some( Country.IS ) )
    val it = Identifier( Language.it, None )
    val it_CH = Identifier( Language.it, Some( Country.CH ) )
    val it_IT = Identifier( Language.it, Some( Country.IT ) )
    val ja = Identifier( Language.ja, None )
    val ja_JP = Identifier( Language.ja, Some( Country.JP ) )
    val ko = Identifier( Language.ko, None )
    val ko_KR = Identifier( Language.ko, Some( Country.KR ) )
    val lt = Identifier( Language.lt, None )
    val lt_LT = Identifier( Language.lt, Some( Country.LT ) )
    val lv = Identifier( Language.lv, None )
    val lv_LV = Identifier( Language.lv, Some( Country.LV ) )
    val mk = Identifier( Language.mk, None )
    val mk_MK = Identifier( Language.mk, Some( Country.MK ) )
    val ms = Identifier( Language.ms, None )
    val ms_MY = Identifier( Language.ms, Some( Country.MY ) )
    val mt = Identifier( Language.mt, None )
    val mt_MT = Identifier( Language.mt, Some( Country.MT ) )
    val nl = Identifier( Language.nl, None )
    val nl_BE = Identifier( Language.nl, Some( Country.BE ) )
    val nl_NL = Identifier( Language.nl, Some( Country.NL ) )
    val no = Identifier( Language.no, None )
    val no_NO = Identifier( Language.no, Some( Country.NO ) )
    val pl = Identifier( Language.pl, None )
    val pl_PL = Identifier( Language.pl, Some( Country.PL ) )
    val pt = Identifier( Language.pt, None )
    val pt_BR = Identifier( Language.pt, Some( Country.BR ) )
    val pt_PT = Identifier( Language.pt, Some( Country.PT ) )
    val ro = Identifier( Language.ro, None )
    val ro_RO = Identifier( Language.ro, Some( Country.RO ) )
    val ru = Identifier( Language.ru, None )
    val ru_RU = Identifier( Language.ru, Some( Country.RU ) )
    val sk = Identifier( Language.sk, None )
    val sk_SK = Identifier( Language.sk, Some( Country.SK ) )
    val sl = Identifier( Language.sl, None )
    val sl_SI = Identifier( Language.sl, Some( Country.SI ) )
    val sq = Identifier( Language.sq, None )
    val sq_AL = Identifier( Language.sq, Some( Country.AL ) )
    val sr = Identifier( Language.sr, None )
    val sr_BA = Identifier( Language.sr, Some( Country.BA ) )
    val sr_ME = Identifier( Language.sr, Some( Country.ME ) )
    val sr_RS = Identifier( Language.sr, Some( Country.RS ) )
    val sv = Identifier( Language.sv, None )
    val sv_SE = Identifier( Language.sv, Some( Country.SE ) )
    val th = Identifier( Language.th, None )
    val th_TH = Identifier( Language.th, Some( Country.TH ) )
    val tr = Identifier( Language.tr, None )
    val tr_TR = Identifier( Language.tr, Some( Country.TR ) )
    val uk = Identifier( Language.uk, None )
    val uk_UA = Identifier( Language.uk, Some( Country.UA ) )
    val vi = Identifier( Language.vi, None )
    val vi_VN = Identifier( Language.vi, Some( Country.VN ) )
    val zh = Identifier( Language.zh, None )
    val zh_CN = Identifier( Language.zh, Some( Country.CN ) )
    val zh_HK = Identifier( Language.zh, Some( Country.HK ) )
    val zh_SG = Identifier( Language.zh, Some( Country.SG ) )
    val zh_TW = Identifier( Language.zh, Some( Country.TW ) )
}