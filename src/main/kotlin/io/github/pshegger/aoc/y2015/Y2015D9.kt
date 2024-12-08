package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.extensions.extractAll
import io.github.pshegger.aoc.common.extensions.permutations
import io.github.pshegger.aoc.common.extensions.toExtractor

class Y2015D9 : BaseSolver() {
    override val year = 2015
    override val day = 9

    private val extractor = "%s to %s = %d".toExtractor()

    override fun part1(): Int = parseInput()
        .getRouteDistances()
        .minOf { it.value }

    override fun part2(): Int = parseInput()
        .getRouteDistances()
        .maxOf { it.value }

    private fun List<Route>.getRouteDistances(): Map<String, Int> {
        val distances = fold(mapOf<String, Int>()) { acc, route ->
            acc + Pair(Pair(route.place1, route.place2).getRouteHash(), route.distance)
        }

        return flatMap { listOf(it.place1, it.place2) }
            .toSet()
            .permutations()
            .associate { fullRoute ->
                val routeDistance = fullRoute.zipWithNext()
                    .fold(0) { fullDistance, (start, destination) ->
                        fullDistance + distances.getDistance(start, destination)
                    }
                Pair(fullRoute.joinToString(separator = " -> "), routeDistance)
            }
    }

    private fun Pair<String, String>.getRouteHash(): String =
        "${minOf(first, second)}:${maxOf(first, second)}"

    private fun Map<String, Int>.getDistance(start: String, destination: String) =
        get(Pair(start, destination).getRouteHash()) ?: Int.MAX_VALUE

    private fun parseInput() = readInput {
        readLines().extractAll(extractor) { (place1, place2, distance) ->
            Route(place1, place2, distance.toInt())
        }
    }

    private data class Route(val place1: String, val place2: String, val distance: Int)
}
