package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.Coordinate
import kotlin.math.roundToInt

class Y2020D3 : BaseSolver() {
    override val year = 2020
    override val day = 3

    override fun part1(): Int =
        parseInput().countTrees(Pair(3, 1))

    override fun part2(): Long = parseInput().let { treeMap ->
        listOf(
            Pair(1, 1),
            Pair(3, 1),
            Pair(5, 1),
            Pair(7, 1),
            Pair(1, 2),
        ).fold(1L) { acc, slope ->
            acc * treeMap.countTrees(slope)
        }
    }

    private fun parseInput() = readInput {
        TreeMap(readLines().map { it.trim() })
    }

    data class TreeMap(val lines: List<String>) {

        fun countTrees(slope: Pair<Int, Int>) = calculateCoords(slope).count { isTree(it) }

        operator fun get(x: Int, y: Int) = lines[y][x % lines[y].length]
        operator fun get(coords: Coordinate) = this[coords.x, coords.y]

        private fun isTree(coords: Coordinate) = this[coords] == '#'

        private fun calculateCoords(slope: Pair<Int, Int>) =
            (0 until (lines.size.toFloat() / slope.second).roundToInt()).map { i ->
                Coordinate(i * slope.first, i * slope.second)
            }
    }
}
