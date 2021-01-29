# Changelog

# 0.1.6

_2021-01-29_

 * Generate `StringFormatN` boilerplate up to n=22
 * Update munit-cats-effect-2 to 0.13.0 (#106)
 * Minor doc enhancements

# 0.1.5

_2021-01-28_

 * Fix `StringFormat.toString` to use 0-based-indices
 * Fix plurals example in docs

# 0.1.4

_2021-01-28_

 * Change `StringFormat` encoding to `MessageFormat` style
 * Add plurals section to docs
 * Add argument section to docs

# 0.1.3

_2021-01-28_

 * Create paradox documentation microsite
 * Update sconfig to 1.4.0 (#104)

# 0.1.2

_2021-01-26_

 * Add cats module
 * Improve sample app

# 0.1.1

_2021-01-26_

 * Allow `Option` decoding for missing fields
 * Fix `DerivedDecoder` giving wrong path in error message
 * Fix hocon loader `null` handling
 * Add numeric `Encoder` instances
 * Add support for `Option` in codecs
 * Add `Dictionary` and `Translations.toMap`
 * Add `Dictionary` collection operations
 * Improve `Translations.toDictionary`
 * Add `Dictionary.of`
 * Add StringFormat tests

# 0.1.0

_2021-01-26_

 * Ditch collection types and go all in on data classes
 * Introduce `StringFormat` for `String` argument injection

# 0.0.1

_2021-01-01_

 * Initial release