package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.extensions.updated
import kotlin.math.pow

class Y2020D10 : BaseSolver() {
    override val year = 2020
    override val day = 10

    override fun part1(): Int {
        val data = parseInput()
        val adapters = (data + listOf(0, data.maxOf { it } + 3)).sorted()
        val jumps = (1 until adapters.size).fold(listOf(0, 0, 0)) { acc, i ->
            val jumpIndex = adapters[i] - adapters[i - 1] - 1
            acc.updated(jumpIndex, acc[jumpIndex] + 1)
        }

        return jumps[0] * jumps[2]
    }

    override fun part2(): Long {
        val data = parseInput()
        val adapters = (data + listOf(0, data.maxOf { it } + 3)).sorted()
        val mustContain = mutableListOf(0)
        (1 until adapters.size - 1).forEach { i ->
            if (adapters[i] + 3 == adapters[i + 1] || adapters[i] - 3 == adapters[i - 1]) {
                mustContain.add(i)
            }
        }
        return (0 until mustContain.size - 1).fold(1L) { acc, i ->
            val between = mustContain[i + 1] - mustContain[i] - 1
            val m = between / 3
            acc * (2.0.pow(between).toLong() - m)
        }
    }

    private fun parseInput() = readInput {
        readLines().map { it.toInt() }
    }
}
