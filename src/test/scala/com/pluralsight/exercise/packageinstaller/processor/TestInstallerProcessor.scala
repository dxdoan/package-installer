package com.pluralsight.exercise.packageinstaller.processor

import scalax.collection.GraphPredef._,
scalax.collection.GraphEdge._
import scalax.collection.constrained._

import InstallerProcessor._

import org.scalatest._

class TestInstallerProcessor extends WordSpec {

  "buildDAG()" when {
    "given a connected dependency specification" should {
      "return a connected graph" in {
        val actual = buildDAG(List(("a", "b"), ("b", "c"), ("", "a")))
        val expected = Graph("a" ~> "b", "b" ~> "c")
        assert(actual === expected)
      }
    }

    "given a disconnected dependency specification" should {
      "return a disconnected graph" in {
        val actual = buildDAG(List(("a", "b"), ("c", "d"), ("", "a"), ("", "c")))
        val expected = Graph("a" ~> "b", "c" ~> "d")
        assert(actual === expected)
      }
    }

    "given a disconnected dependency specification with isolated packages" should {
      "return a disconnected graph with isolated nodes" in {
        val actual = buildDAG(List(("a", "b"), ("", "a"), ("", "c")))
        val expected = Graph("c", "a" ~> "b")
        assert(actual === expected)
      }
    }

    "given a dependency specification with only isolated packages" should {
      "return a graph with only isolated nodes" in {
        val actual = buildDAG(List(("", "a"), ("", "b")))
        val expected = Graph("a", "b")
        assert(actual === expected)
      }
    }

    "given a dependency specification with multiple dependencies" should {
      "return a corresponding graph" in {
        val actual = buildDAG(List(("", "a"), ("", "b"), ("a", "c"), ("b", "c")))
        val expected = Graph("a" ~> "c", "b" ~> "c")
        assert(actual === expected)
      }
    }

    "given a dependency specification with cycles" should {
      "throw an exception" in {
        assertThrows[IllegalArgumentException] {
          buildDAG(List(("a", "b"), ("b", "a")))
        }
      }
    }
  }

  "generateInstallOrder()" when {
    def getIndex(orderWithIndex: List[(String, Int)], packageName: String) = {
      orderWithIndex.filter({ case (p, _) => p == packageName}).map({ case (_, index) => index }).head
    }

    "given a connected dependency specification" should {
      "generate the right order" in {
        val actual = generateInstallOrder(List(("a", "b"), ("b", "c"), ("", "a")))
        val expected = List("a", "b", "c")
        assert(actual === expected)
      }
    }

    "given a disconnected dependency specification" should {
      "generate the right order" in {
        val orderWithIndex = generateInstallOrder(List(("a", "b"), ("c", "d"), ("", "a"), ("", "c"))).zipWithIndex
        val aIndex = getIndex(orderWithIndex, "a")
        val bIndex = getIndex(orderWithIndex, "b")
        val cIndex = getIndex(orderWithIndex, "c")
        val dIndex = getIndex(orderWithIndex, "d")
        assert(aIndex < bIndex)
        assert(cIndex < dIndex)
      }
    }

    "given a disconnected dependency specification with isolated packages" should {
      "generate the right order" in {
        val installOrder = generateInstallOrder(List(("a", "b"), ("", "a"), ("", "c")))
        val orderWithIndex = installOrder.zipWithIndex
        val aIndex = getIndex(orderWithIndex, "a")
        val bIndex = getIndex(orderWithIndex, "b")
        assert(aIndex < bIndex)
        assert(installOrder.contains("c"))
      }
    }

    "given a dependency specification with multiple dependencies" should {
      "generate the right order" in {
        val orderWithIndex = generateInstallOrder(List(("", "a"), ("", "b"), ("a", "c"), ("b", "c"))).zipWithIndex
        val aIndex = getIndex(orderWithIndex, "a")
        val bIndex = getIndex(orderWithIndex, "b")
        val cIndex = getIndex(orderWithIndex, "c")
        assert(aIndex < cIndex)
        assert(bIndex < cIndex)
      }
    }
  }
}