package io.github.pshegger.aoc.y2022

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.splitByBlank

class Y2022D1 : BaseSolver() {
    override val year = 2022
    override val day = 1

    override fun part1(): Int =
        parseInput()
            .maxOf { it.sum() }

    override fun part2(): Int =
        parseInput()
            .map { it.sum() }
            .sortedDescending()
            .take(3)
            .sum()

    private fun parseInput() = readInput {
        readLines()
            .splitByBlank()
            .map { calories ->
                calories.map { it.toInt() }
            }
    }
}
