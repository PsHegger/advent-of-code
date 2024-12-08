package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.utils.getCartesianProduct

class Y2020D17 : BaseSolver() {
    override val year = 2020
    override val day = 17

    override fun part1(): Int = solve(false)
    override fun part2(): Int = solve(true)

    private fun solve(isPart2: Boolean, cycleCount: Int = 6) =
        (1..cycleCount).fold(parseInput(isPart2)) { field, _ ->
            field.simulateCycle()
        }.aliveCount

    private fun parseInput(isPart2: Boolean): Field = readInput {
        readLines()
            .flatMapIndexed { y: Int, line: String ->
                line.mapIndexedNotNull { x, c ->
                    when {
                        c == '#' && isPart2 -> Coord4(x, y, 0, 0)
                        c == '#' -> Coord3(x, y, 0)
                        else -> null
                    }
                }
            }
            .associateWith { true }
            .let { Field(it) }
    }

    private data class Field(private val state: Map<Coord, Boolean>) {
        val aliveCount: Int get() = state.count { it.value }

        fun isAlive(c: Coord): Boolean = state[c] ?: false

        fun aliveNeighborCount(c: Coord) = c.getNeighbors().count { isAlive(it) }

        fun simulateCycle(): Field =
            state
                .filter { (_, alive) -> alive }
                .flatMap { (c, _) -> c.getNeighbors() }
                .toSet()
                .associateWith { c ->
                    val nc = aliveNeighborCount(c)
                    when {
                        isAlive(c) && nc in 2..3 -> true
                        !isAlive(c) && nc == 3 -> true
                        else -> false
                    }
                }
                .let { Field(it) }
    }

    interface Coord {
        fun getNeighbors(): List<Coord>
    }

    data class Coord3(val x: Int, val y: Int, val z: Int) : Coord {

        override fun getNeighbors(): List<Coord> = neighborDiffs.map { diff ->
            Coord3(x + diff.x, y + diff.y, z + diff.z)
        }

        companion object {
            private val neighborDiffs: List<Coord3> =
                (1..3).map { (-1..1) }
                    .getCartesianProduct()
                    .mapNotNull { diff ->
                        if (diff.all { it == 0 }) {
                            null
                        } else {
                            Coord3(diff[0], diff[1], diff[2])
                        }
                    }
        }
    }

    data class Coord4(val x: Int, val y: Int, val z: Int, val w: Int) : Coord {

        override fun getNeighbors(): List<Coord> = neighborDiffs.map { diff ->
            Coord4(x + diff.x, y + diff.y, z + diff.z, w + diff.w)
        }

        companion object {
            private val neighborDiffs: List<Coord4> =
                (1..4).map { (-1..1) }
                    .getCartesianProduct()
                    .mapNotNull { diff ->
                        if (diff.all { it == 0 }) {
                            null
                        } else {
                            Coord4(diff[0], diff[1], diff[2], diff[3])
                        }
                    }
        }
    }
}
