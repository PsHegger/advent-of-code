package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.BaseSolver

class Y2015D10 : BaseSolver() {
    override val year = 2015
    override val day = 10

    override fun part1(): Int = solve(ITERATION_COUNT1)
    override fun part2(): Int = solve(ITERATION_COUNT2)

    private fun solve(iterationCount: Int): Int {
        var result = PUZZLE_INPUT.convertToList()
        repeat(iterationCount - 1) {
            result = result.lookAndSay()
        }
        return result.size  * 2
    }

    private fun List<Pair<Int, Int>>.lookAndSay(): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        forEach { c ->
            if (result.isEmpty() || result.last().second != c.first) {
                if (c.first == c.second) {
                    result.add(Pair(2, c.first))
                } else {
                    result.add(Pair(1, c.first))
                    result.add(Pair(1, c.second))
                }
            } else {
                if (c.first == c.second) {
                    val prev = result.removeLast()
                    result.add(Pair(prev.first + 2, prev.second))
                } else {
                    val prev = result.removeLast()
                    result.add(Pair(prev.first + 1, prev.second))
                    result.add(Pair(1, c.second))
                }
            }
        }
        return result
    }

    private fun Int.convertToList() = toString()
        .fold(emptyList<Pair<Int, Int>>()) { counts, c ->
            val code: Int = c - '0'
            if (counts.isEmpty() || counts.last().second != code) {
                counts + Pair(1, code)
            } else {
                val currentCount = counts.last().first
                counts.dropLast(1) + Pair(currentCount + 1, code)
            }
        }

    companion object {

        private const val PUZZLE_INPUT = 1113222113
        private const val ITERATION_COUNT1 = 40
        private const val ITERATION_COUNT2 = 50
    }
}
