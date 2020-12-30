package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.BaseSolver

class Y2020D15 : BaseSolver() {
    override val year = 2020
    override val day = 15

    override fun part1(): Int = simulateTurns(2020)
    override fun part2(): Int = simulateTurns(30_000_000)

    private fun simulateTurns(turnCount: Int): Int {
        val numbers = mutableMapOf<Int, Int>()
        val start = parseInput()
        var ctr = 1
        start.dropLast(1).forEach {
            numbers[it] = ctr
            ctr++
        }
        var latest = start.last()

        while (ctr < turnCount) {
            val newLatest = ctr - (numbers[latest] ?: ctr)
            numbers[latest] = ctr
            latest = newLatest
            ctr++
        }

        return latest
    }

    private fun parseInput() = readInput {
        readLines().single().split(",").map { it.toInt() }
    }
}
