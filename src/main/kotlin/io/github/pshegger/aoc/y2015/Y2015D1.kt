package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.BaseSolver

class Y2015D1: BaseSolver() {
    override val year = 2015
    override val day = 1

    override fun part1(): Int =
        parseInput()
            .fold(0) { acc, chr ->
                if (chr == '(') acc + 1 else acc - 1
            }

    override fun part2(): Int? {
        val input = parseInput()
        var level = 0
        for (i in input.indices) {
            level += if (input[i] == '(') 1 else -1
            if (level < 0) {
                return i + 1
            }
        }
        return null
    }

    private fun parseInput() =
        readInput { readLines().joinToString() }
}
