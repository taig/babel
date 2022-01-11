<!--- #main --->
# Babel

> Internationalization (i18n) for Scala applications

[![GitHub](https://img.shields.io/github/last-commit/taig/babel)](https://github.com/taig/babel)
[![Maven Central](https://img.shields.io/maven-central/v/io.taig/babel-core_2.13.svg)](https://search.maven.org/search?q=g:io.taig%20AND%20a:babel-*)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.taig/babel-core_2.13?server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/#nexus-search;gav~io.taig~babel-*~~~)
[![License](https://img.shields.io/github/license/taig/babel)](https://raw.githubusercontent.com/taig/babel/main/LICENSE)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

## Features

- First class support for plurals
- Great fit for Scala.js clients, since `java.util.Locale` is not used
- Translation definitions in [HOCON](https://github.com/lightbend/config/blob/master/HOCON.md) format
- Decode translations into data classes
- Easily share translations of a specific language with the frontend in JSON format
- Typesafe alternative to `String.format` or `java.util.MessageFormat` argument injection
- Dependency-free `core` module
<!--- #main --->

Please proceed to [taig.io/babel](https://taig.github.io/babel/) for more information.