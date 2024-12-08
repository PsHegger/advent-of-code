package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2020D1 : BaseSolver() {
    override val year = 2020
    override val day = 1

    override fun part1(): Int {
        val numbers = parseInput()
        numbers.indices.forEach { i ->
            (i until numbers.size).forEach { j ->
                if (numbers[i] + numbers[j] == 2020) {
                    return numbers[i] * numbers[j]
                }
            }
        }
        return -1
    }

    override fun part2(): Int {
        val numbers = parseInput()
        numbers.indices.forEach { i ->
            (i until numbers.size).forEach { j ->
                (j until numbers.size).forEach { k ->
                    if (numbers[i] + numbers[j] + numbers[k] == 2020) {
                        return numbers[i] * numbers[j] * numbers[k]
                    }
                }
            }
        }
        return -1
    }

    private fun parseInput() = readInput {
        readLines().map { it.toInt() }
    }
}
