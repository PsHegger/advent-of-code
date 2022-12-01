package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.splitByBlank

class Y2020D6 : BaseSolver() {
    override val year = 2020
    override val day = 6

    override fun part1(): Int =
        parseInput().sumOf { it.second.toSet().size }

    override fun part2(): Int =
        parseInput().sumOf { group ->
            group.second.toSet().count { x -> group.second.count { it == x } == group.first }
        }

    private fun parseInput(): List<Pair<Int, String>> = readInput {
        readLines().splitByBlank().map { lines ->
            Pair(lines.size, lines.joinToString(""))
        }
    }
}
