package com.pluralsight.exercise.packageinstaller.parser

object InputParser {
  private[parser] val PackageAndDependenciesRegEx = "\\s*:\\s+"

  // Allows one package to have multiple dependencies, as a comma separated list
  // So "a: b" and "a: b, c" are both valid
  private[parser] val DependenciesListRegEx = "\\s*,\\s*"

  private[parser] def parsePackageDependencies(packageAndDependencies: String) = {
    val splitted = packageAndDependencies.split(PackageAndDependenciesRegEx)

    val packageName = splitted(0).trim
    if (packageName == "") {
      throw new IllegalArgumentException("Invalid input")
    }

    // No dependencies
    if (splitted.length == 1) {
      List(("", packageName))
    } else {
      val dependenciesArr = splitted(1).split(DependenciesListRegEx)

      // Still no dependencies
      if (dependenciesArr.length == 0) {
        List(("", packageName))
      } else {
        dependenciesArr(dependenciesArr.length - 1) = dependenciesArr(dependenciesArr.length - 1).trim
        for(dependency <- dependenciesArr.toList) yield (dependency, packageName)
      }

    }
  }

  def parseInput(input: Array[String]) = {
    (for(packageAndDependencies <- input.toList) yield parsePackageDependencies(packageAndDependencies)).flatten
  }
}