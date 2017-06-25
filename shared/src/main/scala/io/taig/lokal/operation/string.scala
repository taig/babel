package io.taig.lokal.operation

import io.taig.lokal.{ Identifier, Localization }

final class string( val context: StringContext ) extends AnyVal {
    private def substitute( arguments: Seq[Any] ): String =
        context.s( arguments: _* )

    def ar( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar, substitute( arguments ) )

    def ar_AE( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_AE, substitute( arguments ) )

    def ar_BH( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_BH, substitute( arguments ) )

    def ar_DZ( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_DZ, substitute( arguments ) )

    def ar_EG( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_EG, substitute( arguments ) )

    def ar_IQ( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_IQ, substitute( arguments ) )

    def ar_JO( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_JO, substitute( arguments ) )

    def ar_KW( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_KW, substitute( arguments ) )

    def ar_LB( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_LB, substitute( arguments ) )

    def ar_LY( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_LY, substitute( arguments ) )

    def ar_MA( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_MA, substitute( arguments ) )

    def ar_OM( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_OM, substitute( arguments ) )

    def ar_QA( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_QA, substitute( arguments ) )

    def ar_SA( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_SA, substitute( arguments ) )

    def ar_SD( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_SD, substitute( arguments ) )

    def ar_SY( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_SY, substitute( arguments ) )

    def ar_TN( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_TN, substitute( arguments ) )

    def ar_YE( arguments: Any* ): Localization[String] =
        Localization( Identifier.ar_YE, substitute( arguments ) )

    def be( arguments: Any* ): Localization[String] =
        Localization( Identifier.be, substitute( arguments ) )

    def be_BY( arguments: Any* ): Localization[String] =
        Localization( Identifier.be_BY, substitute( arguments ) )

    def bg( arguments: Any* ): Localization[String] =
        Localization( Identifier.bg, substitute( arguments ) )

    def bg_BG( arguments: Any* ): Localization[String] =
        Localization( Identifier.bg_BG, substitute( arguments ) )

    def ca( arguments: Any* ): Localization[String] =
        Localization( Identifier.ca, substitute( arguments ) )

    def ca_ES( arguments: Any* ): Localization[String] =
        Localization( Identifier.ca_ES, substitute( arguments ) )

    def cs( arguments: Any* ): Localization[String] =
        Localization( Identifier.cs, substitute( arguments ) )

    def cs_CZ( arguments: Any* ): Localization[String] =
        Localization( Identifier.cs_CZ, substitute( arguments ) )

    def da( arguments: Any* ): Localization[String] =
        Localization( Identifier.da, substitute( arguments ) )

    def da_DK( arguments: Any* ): Localization[String] =
        Localization( Identifier.da_DK, substitute( arguments ) )

    def de( arguments: Any* ): Localization[String] =
        Localization( Identifier.de, substitute( arguments ) )

    def de_AT( arguments: Any* ): Localization[String] =
        Localization( Identifier.de_AT, substitute( arguments ) )

    def de_CH( arguments: Any* ): Localization[String] =
        Localization( Identifier.de_CH, substitute( arguments ) )

    def de_DE( arguments: Any* ): Localization[String] =
        Localization( Identifier.de_DE, substitute( arguments ) )

    def de_GR( arguments: Any* ): Localization[String] =
        Localization( Identifier.de_GR, substitute( arguments ) )

    def de_LU( arguments: Any* ): Localization[String] =
        Localization( Identifier.de_LU, substitute( arguments ) )

    def el( arguments: Any* ): Localization[String] =
        Localization( Identifier.el, substitute( arguments ) )

    def el_CY( arguments: Any* ): Localization[String] =
        Localization( Identifier.el_CY, substitute( arguments ) )

    def el_GR( arguments: Any* ): Localization[String] =
        Localization( Identifier.el_GR, substitute( arguments ) )

    def en( arguments: Any* ): Localization[String] =
        Localization( Identifier.en, substitute( arguments ) )

    def en_AU( arguments: Any* ): Localization[String] =
        Localization( Identifier.en_AU, substitute( arguments ) )

    def en_CA( arguments: Any* ): Localization[String] =
        Localization( Identifier.en_CA, substitute( arguments ) )

    def en_GB( arguments: Any* ): Localization[String] =
        Localization( Identifier.en_GB, substitute( arguments ) )

    def en_IE( arguments: Any* ): Localization[String] =
        Localization( Identifier.en_IE, substitute( arguments ) )

    def en_IN( arguments: Any* ): Localization[String] =
        Localization( Identifier.en_IN, substitute( arguments ) )

    def en_MT( arguments: Any* ): Localization[String] =
        Localization( Identifier.en_MT, substitute( arguments ) )

    def en_NZ( arguments: Any* ): Localization[String] =
        Localization( Identifier.en_NZ, substitute( arguments ) )

    def en_PH( arguments: Any* ): Localization[String] =
        Localization( Identifier.en_PH, substitute( arguments ) )

    def en_SG( arguments: Any* ): Localization[String] =
        Localization( Identifier.en_SG, substitute( arguments ) )

    def en_US( arguments: Any* ): Localization[String] =
        Localization( Identifier.en_US, substitute( arguments ) )

    def en_ZA( arguments: Any* ): Localization[String] =
        Localization( Identifier.en_ZA, substitute( arguments ) )

    def es( arguments: Any* ): Localization[String] =
        Localization( Identifier.es, substitute( arguments ) )

    def es_AR( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_AR, substitute( arguments ) )

    def es_BO( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_BO, substitute( arguments ) )

    def es_CL( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_CL, substitute( arguments ) )

    def es_CO( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_CO, substitute( arguments ) )

    def es_CR( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_CR, substitute( arguments ) )

    def es_CU( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_CU, substitute( arguments ) )

    def es_DO( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_DO, substitute( arguments ) )

    def es_EC( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_EC, substitute( arguments ) )

    def es_ES( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_ES, substitute( arguments ) )

    def es_GT( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_GT, substitute( arguments ) )

    def es_HN( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_HN, substitute( arguments ) )

    def es_MX( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_MX, substitute( arguments ) )

    def es_NI( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_NI, substitute( arguments ) )

    def es_PA( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_PA, substitute( arguments ) )

    def es_PE( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_PE, substitute( arguments ) )

    def es_PR( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_PR, substitute( arguments ) )

    def es_PY( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_PY, substitute( arguments ) )

    def es_SV( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_SV, substitute( arguments ) )

    def es_US( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_US, substitute( arguments ) )

    def es_UY( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_UY, substitute( arguments ) )

    def es_VE( arguments: Any* ): Localization[String] =
        Localization( Identifier.es_VE, substitute( arguments ) )

    def et( arguments: Any* ): Localization[String] =
        Localization( Identifier.et, substitute( arguments ) )

    def et_EE( arguments: Any* ): Localization[String] =
        Localization( Identifier.et_EE, substitute( arguments ) )

    def fi( arguments: Any* ): Localization[String] =
        Localization( Identifier.fi, substitute( arguments ) )

    def fi_FI( arguments: Any* ): Localization[String] =
        Localization( Identifier.fi_FI, substitute( arguments ) )

    def fr( arguments: Any* ): Localization[String] =
        Localization( Identifier.fr, substitute( arguments ) )

    def fr_BE( arguments: Any* ): Localization[String] =
        Localization( Identifier.fr_BE, substitute( arguments ) )

    def fr_CA( arguments: Any* ): Localization[String] =
        Localization( Identifier.fr_CA, substitute( arguments ) )

    def fr_CH( arguments: Any* ): Localization[String] =
        Localization( Identifier.fr_CH, substitute( arguments ) )

    def fr_FR( arguments: Any* ): Localization[String] =
        Localization( Identifier.fr_FR, substitute( arguments ) )

    def fr_LU( arguments: Any* ): Localization[String] =
        Localization( Identifier.fr_LU, substitute( arguments ) )

    def ga( arguments: Any* ): Localization[String] =
        Localization( Identifier.ga, substitute( arguments ) )

    def ga_IE( arguments: Any* ): Localization[String] =
        Localization( Identifier.ga_IE, substitute( arguments ) )

    def hi( arguments: Any* ): Localization[String] =
        Localization( Identifier.hi, substitute( arguments ) )

    def hi_IN( arguments: Any* ): Localization[String] =
        Localization( Identifier.hi_IN, substitute( arguments ) )

    def hr( arguments: Any* ): Localization[String] =
        Localization( Identifier.hr, substitute( arguments ) )

    def hr_HR( arguments: Any* ): Localization[String] =
        Localization( Identifier.hr_HR, substitute( arguments ) )

    def hu( arguments: Any* ): Localization[String] =
        Localization( Identifier.hu, substitute( arguments ) )

    def hu_HU( arguments: Any* ): Localization[String] =
        Localization( Identifier.hu_HU, substitute( arguments ) )

    def is( arguments: Any* ): Localization[String] =
        Localization( Identifier.is, substitute( arguments ) )

    def is_IS( arguments: Any* ): Localization[String] =
        Localization( Identifier.is_IS, substitute( arguments ) )

    def it( arguments: Any* ): Localization[String] =
        Localization( Identifier.it, substitute( arguments ) )

    def it_CH( arguments: Any* ): Localization[String] =
        Localization( Identifier.it_CH, substitute( arguments ) )

    def it_IT( arguments: Any* ): Localization[String] =
        Localization( Identifier.it_IT, substitute( arguments ) )

    def ja( arguments: Any* ): Localization[String] =
        Localization( Identifier.ja, substitute( arguments ) )

    def ja_JP( arguments: Any* ): Localization[String] =
        Localization( Identifier.ja_JP, substitute( arguments ) )

    def ko( arguments: Any* ): Localization[String] =
        Localization( Identifier.ko, substitute( arguments ) )

    def ko_KR( arguments: Any* ): Localization[String] =
        Localization( Identifier.ko_KR, substitute( arguments ) )

    def lt( arguments: Any* ): Localization[String] =
        Localization( Identifier.lt, substitute( arguments ) )

    def lt_LT( arguments: Any* ): Localization[String] =
        Localization( Identifier.lt_LT, substitute( arguments ) )

    def lv( arguments: Any* ): Localization[String] =
        Localization( Identifier.lv, substitute( arguments ) )

    def lv_LV( arguments: Any* ): Localization[String] =
        Localization( Identifier.lv_LV, substitute( arguments ) )

    def mk( arguments: Any* ): Localization[String] =
        Localization( Identifier.mk, substitute( arguments ) )

    def mk_MK( arguments: Any* ): Localization[String] =
        Localization( Identifier.mk_MK, substitute( arguments ) )

    def ms( arguments: Any* ): Localization[String] =
        Localization( Identifier.ms, substitute( arguments ) )

    def ms_MY( arguments: Any* ): Localization[String] =
        Localization( Identifier.ms_MY, substitute( arguments ) )

    def mt( arguments: Any* ): Localization[String] =
        Localization( Identifier.mt, substitute( arguments ) )

    def mt_MT( arguments: Any* ): Localization[String] =
        Localization( Identifier.mt_MT, substitute( arguments ) )

    def nl( arguments: Any* ): Localization[String] =
        Localization( Identifier.nl, substitute( arguments ) )

    def nl_BE( arguments: Any* ): Localization[String] =
        Localization( Identifier.nl_BE, substitute( arguments ) )

    def nl_NL( arguments: Any* ): Localization[String] =
        Localization( Identifier.nl_NL, substitute( arguments ) )

    def no( arguments: Any* ): Localization[String] =
        Localization( Identifier.no, substitute( arguments ) )

    def no_NO( arguments: Any* ): Localization[String] =
        Localization( Identifier.no_NO, substitute( arguments ) )

    def pl( arguments: Any* ): Localization[String] =
        Localization( Identifier.pl, substitute( arguments ) )

    def pl_PL( arguments: Any* ): Localization[String] =
        Localization( Identifier.pl_PL, substitute( arguments ) )

    def pt( arguments: Any* ): Localization[String] =
        Localization( Identifier.pt, substitute( arguments ) )

    def pt_BR( arguments: Any* ): Localization[String] =
        Localization( Identifier.pt_BR, substitute( arguments ) )

    def pt_PT( arguments: Any* ): Localization[String] =
        Localization( Identifier.pt_PT, substitute( arguments ) )

    def ro( arguments: Any* ): Localization[String] =
        Localization( Identifier.ro, substitute( arguments ) )

    def ro_RO( arguments: Any* ): Localization[String] =
        Localization( Identifier.ro_RO, substitute( arguments ) )

    def ru( arguments: Any* ): Localization[String] =
        Localization( Identifier.ru, substitute( arguments ) )

    def ru_RU( arguments: Any* ): Localization[String] =
        Localization( Identifier.ru_RU, substitute( arguments ) )

    def sk( arguments: Any* ): Localization[String] =
        Localization( Identifier.sk, substitute( arguments ) )

    def sk_SK( arguments: Any* ): Localization[String] =
        Localization( Identifier.sk_SK, substitute( arguments ) )

    def sl( arguments: Any* ): Localization[String] =
        Localization( Identifier.sl, substitute( arguments ) )

    def sl_SI( arguments: Any* ): Localization[String] =
        Localization( Identifier.sl_SI, substitute( arguments ) )

    def sq( arguments: Any* ): Localization[String] =
        Localization( Identifier.sq, substitute( arguments ) )

    def sq_AL( arguments: Any* ): Localization[String] =
        Localization( Identifier.sq_AL, substitute( arguments ) )

    def sr( arguments: Any* ): Localization[String] =
        Localization( Identifier.sr, substitute( arguments ) )

    def sr_BA( arguments: Any* ): Localization[String] =
        Localization( Identifier.sr_BA, substitute( arguments ) )

    def sr_ME( arguments: Any* ): Localization[String] =
        Localization( Identifier.sr_ME, substitute( arguments ) )

    def sr_RS( arguments: Any* ): Localization[String] =
        Localization( Identifier.sr_RS, substitute( arguments ) )

    def sv( arguments: Any* ): Localization[String] =
        Localization( Identifier.sv, substitute( arguments ) )

    def sv_SE( arguments: Any* ): Localization[String] =
        Localization( Identifier.sv_SE, substitute( arguments ) )

    def th( arguments: Any* ): Localization[String] =
        Localization( Identifier.th, substitute( arguments ) )

    def th_TH( arguments: Any* ): Localization[String] =
        Localization( Identifier.th_TH, substitute( arguments ) )

    def tr( arguments: Any* ): Localization[String] =
        Localization( Identifier.tr, substitute( arguments ) )

    def tr_TR( arguments: Any* ): Localization[String] =
        Localization( Identifier.tr_TR, substitute( arguments ) )

    def uk( arguments: Any* ): Localization[String] =
        Localization( Identifier.uk, substitute( arguments ) )

    def uk_UA( arguments: Any* ): Localization[String] =
        Localization( Identifier.uk_UA, substitute( arguments ) )

    def vi( arguments: Any* ): Localization[String] =
        Localization( Identifier.vi, substitute( arguments ) )

    def vi_VN( arguments: Any* ): Localization[String] =
        Localization( Identifier.vi_VN, substitute( arguments ) )

    def zh( arguments: Any* ): Localization[String] =
        Localization( Identifier.zh, substitute( arguments ) )

    def zh_CN( arguments: Any* ): Localization[String] =
        Localization( Identifier.zh_CN, substitute( arguments ) )

    def zh_HK( arguments: Any* ): Localization[String] =
        Localization( Identifier.zh_HK, substitute( arguments ) )

    def zh_SG( arguments: Any* ): Localization[String] =
        Localization( Identifier.zh_SG, substitute( arguments ) )

    def zh_TW( arguments: Any* ): Localization[String] =
        Localization( Identifier.zh_TW, substitute( arguments ) )
}