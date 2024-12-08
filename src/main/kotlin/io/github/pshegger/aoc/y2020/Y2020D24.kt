package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.model.Coordinate
import kotlin.math.absoluteValue

class Y2020D24 : BaseSolver() {
    override val year = 2020
    override val day = 24

    override fun part1(): Int =
        calculateStartingState(parseInput()).values.count { it }

    override fun part2(): Int {
        val tiles = parseInput()
        val tileStates = calculateStartingState(tiles)

        val finalState = (1..100).fold(tileStates) { acc, _ -> simulateDay(acc) }

        return finalState.values.count { it }
    }

    private fun simulateDay(starting: Map<Coordinate, Boolean>): Map<Coordinate, Boolean> =
        starting.keys.flatMap { it.adjacentCoords() }
            .toSet()
            .associateWith { c ->
                val adjacentBlacks = c.adjacentCoords().count { starting[it] == true }
                val currentTile = starting[c] ?: false
                when {
                    currentTile && adjacentBlacks == 0 -> false
                    currentTile && adjacentBlacks > 2 -> false
                    !currentTile && adjacentBlacks == 2 -> true
                    else -> currentTile
                }
            }

    private fun calculateStartingState(tiles: List<TilePath>): Map<Coordinate, Boolean> {
        val tileStates = mutableMapOf<Coordinate, Boolean>()

        tiles.forEach { t ->
            val currentState = tileStates[t.coords] ?: false
            tileStates[t.coords] = !currentState
        }
        return tileStates
    }

    private fun parseInput(): List<TilePath> = readInput {
        readLines().map { line ->
            val directions = mutableListOf<Direction>()
            var l = line
            while (l.isNotBlank()) {
                val (d, lr) = parseLine(l)
                directions.add(d)
                l = lr
            }
            TilePath(directions)
        }
    }

    private fun parseLine(line: String) =
        Direction.values().first { line.startsWith(it.v) }.let { dir ->
            Pair(dir, line.drop(dir.v.length))
        }

    data class TilePath(val directions: List<Direction>) {
        val coords by lazy { toCoords() }

        private fun toCoords(): Coordinate =
            directions.fold(Coordinate(0, 0)) { acc, direction -> direction.applyToCoords(acc) }
    }

    private fun Coordinate.adjacentCoords() = Direction.values().map { it.applyToCoords(this) }

    enum class Direction(val v: String) {
        East("e"),
        SouthEast("se"),
        SouthWest("sw"),
        West("w"),
        NorthWest("nw"),
        NorthEast("ne");

        fun applyToCoords(coords: Coordinate): Coordinate {
            val (x, y) = coords

            return when (this) {
                East -> coords.copy(x = x + 1)
                SouthEast -> Coordinate(
                    if (y.absoluteValue % 2 == 1) x + 1 else x,
                    y - 1
                )
                SouthWest -> Coordinate(
                    if (y.absoluteValue % 2 == 0) x - 1 else x,
                    y - 1
                )
                West -> coords.copy(x = x - 1)
                NorthWest -> Coordinate(
                    if (y.absoluteValue % 2 == 0) x - 1 else x,
                    y + 1
                )
                NorthEast -> Coordinate(
                    if (y.absoluteValue % 2 == 1) x + 1 else x,
                    y + 1
                )
            }
        }
    }
}
