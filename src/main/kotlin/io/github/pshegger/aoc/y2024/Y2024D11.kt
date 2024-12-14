package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.utils.memoize

class Y2024D11 : BaseSolver() {
    override val year = 2024
    override val day = 11

    private val memoizedCalculateSize = ::calculateSize.memoize()

    override fun part1(): Long = parseInput().sumOf { memoizedCalculateSize(it, 25) }
    override fun part2(): Long = parseInput().sumOf { memoizedCalculateSize(it, 75) }

    private fun calculateSize(stone: Long, remainingSteps: Int): Long {
        if (remainingSteps == 0) {
            return 1
        }

        val count = when {
            stone == 0L -> memoizedCalculateSize(1L, remainingSteps - 1)
            stone.canSplit -> stone.split().toList().sumOf { memoizedCalculateSize(it, remainingSteps - 1) }
            else -> memoizedCalculateSize(stone * 2024L, remainingSteps - 1)
        }
        return count
    }

    private val Long.canSplit: Boolean
        get() = toString().length % 2 == 0

    private fun Long.split(): Pair<Long, Long> {
        val str = toString()
        return Pair(str.take(str.length / 2).toLong(), str.drop(str.length / 2).toLong())
    }

    private fun parseInput() = readInput {
        readLines()[0].split(" ").map { it.toLong() }
    }
}