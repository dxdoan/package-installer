package com.pluralsight.exercise.packageinstaller.parser

import InputParser._

import org.scalatest._

class TestInputParser extends WordSpec {
  "Parse package name" when {
    "standard specification of package name" should {
      "succeed" in {
        val splitted = "a: b".split(PackageAndDependenciesRegEx)
        assert(splitted(0) === "a")
      }
    }

    "there are white spaces after package name" should {
      "succeed" in {
        val splitted = "a   : b".split(PackageAndDependenciesRegEx)
        assert(splitted(0) === "a")
      }
    }

    "there is no package name" should {
      "reflect that fact" in {
        val splitted = "  : b".split(PackageAndDependenciesRegEx)
        assert(splitted(0) === "")
      }
    }

    "there is no white space preceding the dependencies list" should {
      "fail to parse" in {
        val splitted = "a:b".split(PackageAndDependenciesRegEx)
        assert(splitted.length === 1)
      }
    }
  }

  "Parse dependencies list" when {
    "there is one dependency" should {
      "succeed" in {
        val splitted = "a".split(DependenciesListRegEx)
        assert(splitted(0) === "a")
      }
    }

    "there are multiple dependencies" should {
      "succeed" in {
        val splitted = "a, b".split(DependenciesListRegEx)
        assert(splitted(0) === "a")
        assert(splitted(1) === "b")
      }
    }

    "there are extra white spaces and commas" should {
      "succeed" in {
        val splitted = "a , b  ,, , ".split(DependenciesListRegEx)
        assert(splitted.length === 2)
        assert(splitted(0) === "a")
        assert(splitted(1) === "b")
      }
    }

    "the list is actually empty" should {
      "succeed" in {
        val splitted = ",   ,, , ".split(DependenciesListRegEx)
        assert(splitted.length === 0)
      }
    }
  }

  "parsePackageDependencies()" when {
    "there is no package name" should {
      "throw an exception" in {
        assertThrows[IllegalArgumentException] {
          parsePackageDependencies("  : b")
        }
      }
    }

//    "there is no dependency" should {
//      "return a single element list" in {
//        assert(parsePackageDependencies("a: ") === List(("", "a")))
//      }
//    }

    "there is a single dependency" should {
      "return a single element list" in {
        assert(parsePackageDependencies("a: b") === List(("b", "a")))
      }
    }

    "there are multiple dependencies" should {
      "return multiple elements list" in {
        assert(parsePackageDependencies("a: b, c") === List(("b", "a"), ("c", "a")))
      }
    }
  }

  "parseInput()" when {
    "given an array of dependencies defining strings" should {
      "return a list of tuples representing dependencies" in {
        assert(parseInput(Array("a: ", "b: a", "c: a")) === List(("", "a"), ("a", "b"), ("a", "c")))
      }
    }

    "given an array of dependencies defining strings containing invalid input" should {
      "throw an exception" in {
        assertThrows[IllegalArgumentException] {
          parseInput(Array("a: ", "  : a", "c: a"))
        }
      }
    }
  }
}