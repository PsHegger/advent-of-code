package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.extensions.md5

class Y2015D4: BaseSolver() {
    override val year = 2015
    override val day = 4

    override fun part1(): Int = solveForZeros(5)
    override fun part2(): Int = solveForZeros(6)

    private fun solveForZeros(count: Int): Int {
        val secretKey = parseInput()
        var ctr = 1
        val start = buildString { repeat(count) { append("0") } }
        while (true) {
            if ("$secretKey$ctr".md5().startsWith(start)) {
                return ctr
            }
            ctr++
        }
    }

    private fun parseInput(): String =
        readInput { readLines().joinToString() }
}
