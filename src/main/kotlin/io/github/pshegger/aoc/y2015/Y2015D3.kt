package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.model.Coordinate
import java.lang.IllegalStateException

class Y2015D3 : BaseSolver() {
    override val year = 2015
    override val day = 3

    override fun part1(): Int =
        solveForPeopleCount(1)

    override fun part2(): Int =
        solveForPeopleCount(2)

    private fun solveForPeopleCount(peopleCount: Int): Int {
        val directions = parseInput()
        val pos = (0 until peopleCount).map { Coordinate(0, 0) }.toMutableList()
        val visited = mutableSetOf(Coordinate(0, 0))
        directions.forEachIndexed { i, direction ->
            val coords = pos[i % peopleCount]
            val newCoords = direction.move(coords)
            visited.add(newCoords)
            pos[i % peopleCount] = newCoords
        }
        return visited.size
    }

    private fun parseInput(): List<Direction> =
        readInput {
            readLines().joinToString()
                .map {
                    when (it) {
                        '^' -> Direction.North
                        '>' -> Direction.East
                        'v' -> Direction.South
                        '<' -> Direction.West
                        else -> throw IllegalStateException("Unknown direction: $it")
                    }
                }
        }

    private enum class Direction {
        North, East, South, West;

        fun move(coords: Coordinate) = when (this) {
            North -> coords.copy(y = coords.y + 1)
            East -> coords.copy(x = coords.x + 1)
            South -> coords.copy(y = coords.y - 1)
            West -> coords.copy(x = coords.x - 1)
        }
    }
}
