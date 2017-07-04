package com.pluralsight.exercise.packageinstaller

import com.pluralsight.exercise.packageinstaller.parser.InputParser.parseInput
import com.pluralsight.exercise.packageinstaller.processor.InstallerProcessor.generateInstallOrder

object PackageInstaller {
  def main(args: Array[String]) {
    if (args.size < 1) {
      println("Missing dependency specification")

      System.exit(1)
    } else {
      val dependencySpec = parseInput(args)
      val installOrder = generateInstallOrder(dependencySpec)

      println("Package Install Order: " + installOrder.mkString(", "))
    }
  }
}
