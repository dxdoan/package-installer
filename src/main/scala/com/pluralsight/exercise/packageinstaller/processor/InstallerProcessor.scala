package com.pluralsight.exercise.packageinstaller.processor

import scalax.collection.GraphPredef._,
scalax.collection.GraphEdge._
import scalax.collection.constrained._
import scalax.collection.constrained.constraints.Acyclic


object InstallerProcessor {
  // Make sure working on an acyclic graph
  implicit val conf: Config = Acyclic

  private[processor] def buildDAG(dependencies: List[(String, String)]): Graph[String, DiEdge] = {
    // Determine only real edges of the graph
    // e.g. "" -> "a" is excluded
    val toBeEdges = dependencies.filter({ case (source, sink) => source != ""})
    val edges = toBeEdges.map({ case (source, sink) => source ~> sink})

    // Determine only "isolated islands" of the graph
    // e.g. nodes that have no dependencies, also that are not dependencies of any other
    val nodes = dependencies.filter({ case (source, sink) => source == "" }).map({ case (_, sink) => sink }).
                diff(toBeEdges.map({ case (source, _) => source}))

    val g = Graph.from(nodes, edges)

    // Cyclic graph
    if (g.isEmpty) {
      throw new IllegalArgumentException("Dependency Specification contains cycles!")
    }

    g
  }

  def generateInstallOrder(dependencies: List[(String, String)]) = {
    val g = buildDAG(dependencies)

    g.topologicalSort.right.toOption.get.toList
  }
}