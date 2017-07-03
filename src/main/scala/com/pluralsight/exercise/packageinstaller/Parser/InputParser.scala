package com.pluralsight.exercise.packageinstaller.Parser

object InputParser {
  private[Parser] val PackageAndDependenciesRegEx = "\\s*:\\s+"

  // Allows one package to have multiple dependencies, as a comma separated list
  // So "a: b" and "a: b, c" are both valid
  private[Parser] val DependenciesListRegEx = "\\s*,\\s*"

  private[Parser] def parsePackageDependencies(packageAndDependencies: String) = {
    val splitted = packageAndDependencies.split(PackageAndDependenciesRegEx)

    val packageName = splitted(0).trim
    if (packageName == "") {
      throw new IllegalArgumentException("Invalid input")
    }

    // No dependencies
    if (splitted.length == 1) {
      List(("", packageName))
    } else {
      val dependenciesList = splitted(1).split(DependenciesListRegEx)

      // Still no dependencies
      if (dependenciesList.length == 0) {
        List(("", packageName))
      } else {
        for(dependency <- dependenciesList) yield (dependency, packageName)
      }

    }
  }
}