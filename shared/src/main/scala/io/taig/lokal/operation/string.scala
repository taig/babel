package io.taig.lokal.operation

import io.taig.lokal.{ Identifier, Localization, Translation }

final class string( val context: StringContext ) extends AnyVal {
    private def substitude(
        identifier: Identifier,
        arguments:  Seq[Any]
    ): String = {
        val translated = arguments.map {
            case translation @ Translation( _ ) ⇒
                translation.translate( identifier )
            case Localization( _, value ) ⇒ value
            case default                  ⇒ default
        }

        context.s( translated: _* )
    }

    def ar( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar,
            substitude( Identifier.ar, arguments )
        )

    def ar_AE( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_AE,
            substitude( Identifier.ar_AE, arguments )
        )

    def ar_BH( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_BH,
            substitude( Identifier.ar_BH, arguments )
        )

    def ar_DZ( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_DZ,
            substitude( Identifier.ar_DZ, arguments )
        )

    def ar_EG( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_EG,
            substitude( Identifier.ar_EG, arguments )
        )

    def ar_IQ( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_IQ,
            substitude( Identifier.ar_IQ, arguments )
        )

    def ar_JO( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_JO,
            substitude( Identifier.ar_JO, arguments )
        )

    def ar_KW( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_KW,
            substitude( Identifier.ar_KW, arguments )
        )

    def ar_LB( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_LB,
            substitude( Identifier.ar_LB, arguments )
        )

    def ar_LY( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_LY,
            substitude( Identifier.ar_LY, arguments )
        )

    def ar_MA( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_MA,
            substitude( Identifier.ar_MA, arguments )
        )

    def ar_OM( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_OM,
            substitude( Identifier.ar_OM, arguments )
        )

    def ar_QA( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_QA,
            substitude( Identifier.ar_QA, arguments )
        )

    def ar_SA( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_SA,
            substitude( Identifier.ar_SA, arguments )
        )

    def ar_SD( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_SD,
            substitude( Identifier.ar_SD, arguments )
        )

    def ar_SY( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_SY,
            substitude( Identifier.ar_SY, arguments )
        )

    def ar_TN( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_TN,
            substitude( Identifier.ar_TN, arguments )
        )

    def ar_YE( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ar_YE,
            substitude( Identifier.ar_YE, arguments )
        )

    def be( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.be,
            substitude( Identifier.be, arguments )
        )

    def be_BY( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.be_BY,
            substitude( Identifier.be_BY, arguments )
        )

    def bg( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.bg,
            substitude( Identifier.bg, arguments )
        )

    def bg_BG( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.bg_BG,
            substitude( Identifier.bg_BG, arguments )
        )

    def ca( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ca,
            substitude( Identifier.ca, arguments )
        )

    def ca_ES( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ca_ES,
            substitude( Identifier.ca_ES, arguments )
        )

    def cs( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.cs,
            substitude( Identifier.cs, arguments )
        )

    def cs_CZ( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.cs_CZ,
            substitude( Identifier.cs_CZ, arguments )
        )

    def da( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.da,
            substitude( Identifier.da, arguments )
        )

    def da_DK( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.da_DK,
            substitude( Identifier.da_DK, arguments )
        )

    def de( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.de,
            substitude( Identifier.de, arguments )
        )

    def de_AT( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.de_AT,
            substitude( Identifier.de_AT, arguments )
        )

    def de_CH( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.de_CH,
            substitude( Identifier.de_CH, arguments )
        )

    def de_DE( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.de_DE,
            substitude( Identifier.de_DE, arguments )
        )

    def de_GR( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.de_GR,
            substitude( Identifier.de_GR, arguments )
        )

    def de_LU( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.de_LU,
            substitude( Identifier.de_LU, arguments )
        )

    def el( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.el,
            substitude( Identifier.el, arguments )
        )

    def el_CY( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.el_CY,
            substitude( Identifier.el_CY, arguments )
        )

    def el_GR( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.el_GR,
            substitude( Identifier.el_GR, arguments )
        )

    def en( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.en,
            substitude( Identifier.en, arguments )
        )

    def en_AU( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.en_AU,
            substitude( Identifier.en_AU, arguments )
        )

    def en_CA( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.en_CA,
            substitude( Identifier.en_CA, arguments )
        )

    def en_GB( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.en_GB,
            substitude( Identifier.en_GB, arguments )
        )

    def en_IE( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.en_IE,
            substitude( Identifier.en_IE, arguments )
        )

    def en_IN( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.en_IN,
            substitude( Identifier.en_IN, arguments )
        )

    def en_MT( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.en_MT,
            substitude( Identifier.en_MT, arguments )
        )

    def en_NZ( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.en_NZ,
            substitude( Identifier.en_NZ, arguments )
        )

    def en_PH( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.en_PH,
            substitude( Identifier.en_PH, arguments )
        )

    def en_SG( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.en_SG,
            substitude( Identifier.en_SG, arguments )
        )

    def en_US( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.en_US,
            substitude( Identifier.en_US, arguments )
        )

    def en_ZA( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.en_ZA,
            substitude( Identifier.en_ZA, arguments )
        )

    def es( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es,
            substitude( Identifier.es, arguments )
        )

    def es_AR( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_AR,
            substitude( Identifier.es_AR, arguments )
        )

    def es_BO( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_BO,
            substitude( Identifier.es_BO, arguments )
        )

    def es_CL( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_CL,
            substitude( Identifier.es_CL, arguments )
        )

    def es_CO( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_CO,
            substitude( Identifier.es_CO, arguments )
        )

    def es_CR( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_CR,
            substitude( Identifier.es_CR, arguments )
        )

    def es_CU( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_CU,
            substitude( Identifier.es_CU, arguments )
        )

    def es_DO( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_DO,
            substitude( Identifier.es_DO, arguments )
        )

    def es_EC( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_EC,
            substitude( Identifier.es_EC, arguments )
        )

    def es_ES( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_ES,
            substitude( Identifier.es_ES, arguments )
        )

    def es_GT( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_GT,
            substitude( Identifier.es_GT, arguments )
        )

    def es_HN( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_HN,
            substitude( Identifier.es_HN, arguments )
        )

    def es_MX( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_MX,
            substitude( Identifier.es_MX, arguments )
        )

    def es_NI( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_NI,
            substitude( Identifier.es_NI, arguments )
        )

    def es_PA( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_PA,
            substitude( Identifier.es_PA, arguments )
        )

    def es_PE( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_PE,
            substitude( Identifier.es_PE, arguments )
        )

    def es_PR( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_PR,
            substitude( Identifier.es_PR, arguments )
        )

    def es_PY( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_PY,
            substitude( Identifier.es_PY, arguments )
        )

    def es_SV( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_SV,
            substitude( Identifier.es_SV, arguments )
        )

    def es_US( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_US,
            substitude( Identifier.es_US, arguments )
        )

    def es_UY( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_UY,
            substitude( Identifier.es_UY, arguments )
        )

    def es_VE( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.es_VE,
            substitude( Identifier.es_VE, arguments )
        )

    def et( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.et,
            substitude( Identifier.et, arguments )
        )

    def et_EE( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.et_EE,
            substitude( Identifier.et_EE, arguments )
        )

    def fi( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.fi,
            substitude( Identifier.fi, arguments )
        )

    def fi_FI( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.fi_FI,
            substitude( Identifier.fi_FI, arguments )
        )

    def fr( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.fr,
            substitude( Identifier.fr, arguments )
        )

    def fr_BE( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.fr_BE,
            substitude( Identifier.fr_BE, arguments )
        )

    def fr_CA( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.fr_CA,
            substitude( Identifier.fr_CA, arguments )
        )

    def fr_CH( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.fr_CH,
            substitude( Identifier.fr_CH, arguments )
        )

    def fr_FR( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.fr_FR,
            substitude( Identifier.fr_FR, arguments )
        )

    def fr_LU( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.fr_LU,
            substitude( Identifier.fr_LU, arguments )
        )

    def ga( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ga,
            substitude( Identifier.ga, arguments )
        )

    def ga_IE( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ga_IE,
            substitude( Identifier.ga_IE, arguments )
        )

    def hi( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.hi,
            substitude( Identifier.hi, arguments )
        )

    def hi_IN( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.hi_IN,
            substitude( Identifier.hi_IN, arguments )
        )

    def hr( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.hr,
            substitude( Identifier.hr, arguments )
        )

    def hr_HR( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.hr_HR,
            substitude( Identifier.hr_HR, arguments )
        )

    def hu( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.hu,
            substitude( Identifier.hu, arguments )
        )

    def hu_HU( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.hu_HU,
            substitude( Identifier.hu_HU, arguments )
        )

    def is( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.is,
            substitude( Identifier.is, arguments )
        )

    def is_IS( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.is_IS,
            substitude( Identifier.is_IS, arguments )
        )

    def it( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.it,
            substitude( Identifier.it, arguments )
        )

    def it_CH( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.it_CH,
            substitude( Identifier.it_CH, arguments )
        )

    def it_IT( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.it_IT,
            substitude( Identifier.it_IT, arguments )
        )

    def ja( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ja,
            substitude( Identifier.ja, arguments )
        )

    def ja_JP( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ja_JP,
            substitude( Identifier.ja_JP, arguments )
        )

    def ko( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ko,
            substitude( Identifier.ko, arguments )
        )

    def ko_KR( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ko_KR,
            substitude( Identifier.ko_KR, arguments )
        )

    def lt( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.lt,
            substitude( Identifier.lt, arguments )
        )

    def lt_LT( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.lt_LT,
            substitude( Identifier.lt_LT, arguments )
        )

    def lv( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.lv,
            substitude( Identifier.lv, arguments )
        )

    def lv_LV( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.lv_LV,
            substitude( Identifier.lv_LV, arguments )
        )

    def mk( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.mk,
            substitude( Identifier.mk, arguments )
        )

    def mk_MK( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.mk_MK,
            substitude( Identifier.mk_MK, arguments )
        )

    def ms( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ms,
            substitude( Identifier.ms, arguments )
        )

    def ms_MY( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ms_MY,
            substitude( Identifier.ms_MY, arguments )
        )

    def mt( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.mt,
            substitude( Identifier.mt, arguments )
        )

    def mt_MT( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.mt_MT,
            substitude( Identifier.mt_MT, arguments )
        )

    def nl( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.nl,
            substitude( Identifier.nl, arguments )
        )

    def nl_BE( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.nl_BE,
            substitude( Identifier.nl_BE, arguments )
        )

    def nl_NL( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.nl_NL,
            substitude( Identifier.nl_NL, arguments )
        )

    def no( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.no,
            substitude( Identifier.no, arguments )
        )

    def no_NO( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.no_NO,
            substitude( Identifier.no_NO, arguments )
        )

    def pl( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.pl,
            substitude( Identifier.pl, arguments )
        )

    def pl_PL( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.pl_PL,
            substitude( Identifier.pl_PL, arguments )
        )

    def pt( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.pt,
            substitude( Identifier.pt, arguments )
        )

    def pt_BR( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.pt_BR,
            substitude( Identifier.pt_BR, arguments )
        )

    def pt_PT( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.pt_PT,
            substitude( Identifier.pt_PT, arguments )
        )

    def ro( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ro,
            substitude( Identifier.ro, arguments )
        )

    def ro_RO( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ro_RO,
            substitude( Identifier.ro_RO, arguments )
        )

    def ru( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ru,
            substitude( Identifier.ru, arguments )
        )

    def ru_RU( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.ru_RU,
            substitude( Identifier.ru_RU, arguments )
        )

    def sk( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.sk,
            substitude( Identifier.sk, arguments )
        )

    def sk_SK( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.sk_SK,
            substitude( Identifier.sk_SK, arguments )
        )

    def sl( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.sl,
            substitude( Identifier.sl, arguments )
        )

    def sl_SI( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.sl_SI,
            substitude( Identifier.sl_SI, arguments )
        )

    def sq( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.sq,
            substitude( Identifier.sq, arguments )
        )

    def sq_AL( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.sq_AL,
            substitude( Identifier.sq_AL, arguments )
        )

    def sr( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.sr,
            substitude( Identifier.sr, arguments )
        )

    def sr_BA( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.sr_BA,
            substitude( Identifier.sr_BA, arguments )
        )

    def sr_ME( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.sr_ME,
            substitude( Identifier.sr_ME, arguments )
        )

    def sr_RS( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.sr_RS,
            substitude( Identifier.sr_RS, arguments )
        )

    def sv( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.sv,
            substitude( Identifier.sv, arguments )
        )

    def sv_SE( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.sv_SE,
            substitude( Identifier.sv_SE, arguments )
        )

    def th( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.th,
            substitude( Identifier.th, arguments )
        )

    def th_TH( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.th_TH,
            substitude( Identifier.th_TH, arguments )
        )

    def tr( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.tr,
            substitude( Identifier.tr, arguments )
        )

    def tr_TR( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.tr_TR,
            substitude( Identifier.tr_TR, arguments )
        )

    def uk( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.uk,
            substitude( Identifier.uk, arguments )
        )

    def uk_UA( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.uk_UA,
            substitude( Identifier.uk_UA, arguments )
        )

    def vi( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.vi,
            substitude( Identifier.vi, arguments )
        )

    def vi_VN( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.vi_VN,
            substitude( Identifier.vi_VN, arguments )
        )

    def zh( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.zh,
            substitude( Identifier.zh, arguments )
        )

    def zh_CN( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.zh_CN,
            substitude( Identifier.zh_CN, arguments )
        )

    def zh_HK( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.zh_HK,
            substitude( Identifier.zh_HK, arguments )
        )

    def zh_SG( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.zh_SG,
            substitude( Identifier.zh_SG, arguments )
        )

    def zh_TW( arguments: Any* ): Localization[String] =
        Localization(
            Identifier.zh_TW,
            substitude( Identifier.zh_TW, arguments )
        )
}