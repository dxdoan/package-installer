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

    "given a dependency specification with cycles" should {
      "throw an exception" in {
        assertThrows[IllegalArgumentException] {
          buildDAG(List(("a", "b"), ("b", "a")))
        }
      }
    }
  }
}