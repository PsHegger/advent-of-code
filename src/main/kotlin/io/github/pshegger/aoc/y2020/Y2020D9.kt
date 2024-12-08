package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2020D9 : BaseSolver() {
    override val year = 2020
    override val day = 9

    override fun part1(): Long = parseInput().let { data ->
        (25 until data.size).first { !it.hasPrevSum(data) }.let { data[it] }
    }

    override fun part2(): Long {
        val data = parseInput()
        val targetNumber = part1()
        data.indices.forEach { start ->
            var end = start + 1
            var s = data[start] + data[end]
            while (s < targetNumber) {
                end++
                s += data[end]
            }
            if (s == targetNumber) {
                val numbers = data.slice(start..end)
                return numbers.minOf { it } + numbers.maxOf { it }
            }
        }

        return -1
    }

    private fun Int.hasPrevSum(data: List<Long>): Boolean {
        (this - 25 until this).forEach { n0 ->
            (n0 until this).forEach { n1 ->
                if (data[n0] + data[n1] == data[this]) {
                    return true
                }
            }
        }
        return false
    }

    private fun parseInput() = readInput {
        readLines().map { it.toLong() }
    }
}
