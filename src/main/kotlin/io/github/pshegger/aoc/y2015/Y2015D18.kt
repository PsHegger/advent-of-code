package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.model.Coordinate

class Y2015D18 : BaseSolver() {
    override val year = 2015
    override val day = 18

    override fun part1(): Int = solveForCase()
    override fun part2(): Int = solveForCase(
        listOf(
            Coordinate(0, 0),
            Coordinate(0, GRID_SIZE - 1),
            Coordinate(GRID_SIZE - 1, 0),
            Coordinate(GRID_SIZE - 1, GRID_SIZE - 1)
        )
    )

    private fun solveForCase(alwaysOnLights: List<Coordinate> = emptyList()): Int {
        var grid = parseInput()
        repeat(STEP_COUNT) {
            grid = grid.simulateStep(alwaysOnLights)
        }
        return grid.sumOf { line -> line.count { it } }
    }

    private fun List<List<Boolean>>.simulateStep(alwaysOnLights: List<Coordinate>) = (0 until GRID_SIZE).map { y ->
        (0 until GRID_SIZE).map { x ->
            val count = adjacentOnCounts(Coordinate(x, y), alwaysOnLights)
            val current = get(y)[x]
            when {
                Coordinate(x, y) in alwaysOnLights -> true
                current -> count == 2 || count == 3
                else -> count == 3
            }
        }
    }

    private fun List<List<Boolean>>.adjacentOnCounts(coordinate: Coordinate, alwaysOnLights: List<Coordinate>): Int =
        coordinate.adjacents.count { c ->
            if (c.y < 0 || c.y >= size || c.x < 0 || c.x >= get(c.y).size) {
                false
            } else {
                get(c.y)[c.x] || c in alwaysOnLights
            }
        }

    private fun parseInput() = readInput {
        readLines().map { line ->
            line.trim().map { it == '#' }
        }
    }

    companion object {
        const val GRID_SIZE = 100
        const val STEP_COUNT = 100
    }
}
