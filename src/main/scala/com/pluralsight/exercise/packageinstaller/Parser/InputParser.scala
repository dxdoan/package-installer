package com.pluralsight.exercise.packageinstaller.Parser

object InputParser {
  private[Parser] val PackageAndDependenciesRegEx = "\\s*:\\s+"

  // Allows one package to have multiple dependencies, as a comma separated list
  // So "a: b" and "a: b, c" are both valid
  private[Parser] val DependenciesListRegEx = "\\s*,\\s*"
}