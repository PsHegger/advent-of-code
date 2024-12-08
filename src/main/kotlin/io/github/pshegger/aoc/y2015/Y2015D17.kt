package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.BaseSolver

class Y2015D17 : BaseSolver() {
    override val year = 2015
    override val day = 17

    override fun part1(): Int =
        findArrangements(parseInput(), EGGNOG_AMOUNT).size

    override fun part2(): Int {
        val arrangements = findArrangements(parseInput(), EGGNOG_AMOUNT)
        val minContainers = arrangements.minOf { it.size }
        return arrangements.count { it.size == minContainers }
    }

    private fun findArrangements(containers: List<Int>, target: Int): List<List<Int>> {
        val arrangements = mutableListOf<List<Int>>()

        for (i in containers.indices) {
            val container = containers[i]
            when {
                container == target -> arrangements.add(listOf(target))
                container < target -> {
                    arrangements.addAll(
                        findArrangements(containers.drop(i + 1), target - container).map { it + container }
                    )
                }
            }
        }

        return arrangements
    }

    private fun parseInput() = readInput {
        readLines().map { it.trim().toInt() }
    }

    companion object {
        private const val EGGNOG_AMOUNT = 150
    }
}
