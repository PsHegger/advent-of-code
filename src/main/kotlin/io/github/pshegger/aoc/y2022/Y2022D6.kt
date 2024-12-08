package io.github.pshegger.aoc.y2022

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2022D6 : BaseSolver() {
    override val year = 2022
    override val day = 6

    override fun part1(): Int = solveForWindowSize(4)
    override fun part2(): Int = solveForWindowSize(14)

    private fun solveForWindowSize(windowSize: Int) =
        parseInput()
            .windowed(size = windowSize, step = 1)
            .indexOfFirst { it.toSet().size == windowSize } + windowSize

    private fun parseInput() = readInput {
        readLines()[0]
    }
}
