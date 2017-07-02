package com.pluralsight.exercise.packageinstaller.Parser

import InputParser._

import org.scalatest._

class TestInputParser extends WordSpec {
  "Parse package name" when {
    "standard specification of package name" should {
      "succeed" in {
        val splitted = "a: b".split(PackageAndDependenciesRegEx)
        assert(splitted(0) == "a")
      }
    }

    "there are white spaces after package name" should {
      "succeed" in {
        val splitted = "a   : b".split(PackageAndDependenciesRegEx)
        assert(splitted(0) == "a")
      }
    }

    "there is no package name" should {
      "reflect that fact" in {
        val splitted = "  : b".split(PackageAndDependenciesRegEx)
        assert(splitted(0) == "")
      }
    }

    "there is no white space preceding the dependencies list" should {
      "fail to parse" in {
        val splitted = "a:b".split(PackageAndDependenciesRegEx)
        assert(splitted.length == 1)
      }
    }
  }

  "Parse dependencies list" when {
    "there is one dependency" should {
      "succeed" in {
        val splitted = "a".split(DependenciesListRegEx)
        assert(splitted(0) == "a")
      }
    }

    "there are multiple dependencies" should {
      "succeed" in {
        val splitted = "a, b".split(DependenciesListRegEx)
        assert(splitted(0) == "a")
        assert(splitted(1) == "b")
      }
    }

    "there are extra white spaces and commas" should {
      "succeed" in {
        val splitted = "a , b  ,, , ".split(DependenciesListRegEx)
        assert(splitted.length == 2)
        assert(splitted(0) == "a")
        assert(splitted(1) == "b")
      }
    }
  }
}