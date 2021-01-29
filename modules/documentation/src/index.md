@@@ index

* [Installation](installation.md)
* [Guide](guide.md)
* [Sample application](sample.md)
* [Cookbook](cookbook.md)
* [Changelog](changelog.md)

@@@

# Babel

> Internationalization (i18n) for Scala applications
>
> [![GitLab CI](https://gitlab.com/slozzer/babel/badges/master/pipeline.svg?style=flat-square)](https://gitlab.com/slozzer/babel/pipelines)
[![Maven Central](https://img.shields.io/maven-central/v/net.slozzer/babel-core_2.13.svg?style=flat-square)](https://search.maven.org/search?q=g:net.slozzer%20AND%20a:babel-*)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/net.slozzer/babel-core_2.13?server=https%3A%2F%2Foss.sonatype.org&style=flat-square)](https://oss.sonatype.org/#nexus-search;gav~net.slozzer~babel-*~~~)
[![License](https://img.shields.io/github/license/slozzer/babel?style=flat-square)](https://raw.githubusercontent.com/slozzer/babel/master/LICENSE)

## Features

- First class support for plurals
- Great fit for Scala.js clients, since `java.util.Locale` is not used
- Translation definitions in [HOCON](https://github.com/lightbend/config/blob/master/HOCON.md) format
- Decode translations into data classes
- Easily share translations of a specific language with the frontend in JSON format
- Typesafe alternative to `String.format` or `java.util.MessageFormat` argument injection
- Dependency-free `core` module