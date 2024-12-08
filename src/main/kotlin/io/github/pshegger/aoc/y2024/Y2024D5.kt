package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.extensions.splitByBlank

class Y2024D5 : BaseSolver() {
    override val year = 2024
    override val day = 5

    private val input by lazy { parseInput() }
    private val rules by lazy { input.first }
    private val updates by lazy { input.second }

    override fun part1() = updates
        .filter { isOrderCorrect(it) }
        .sumOf { it[it.size / 2] }

    override fun part2() = updates
        .filterNot { isOrderCorrect(it) }
        .map { correctedOrder(it) }
        .sumOf { it[it.size / 2] }

    private fun isOrderCorrect(update: List<Int>): Boolean {
        for (i in update.indices) {
            val page = update[i]
            val requiredPredecessors = rules
                .filter { it.second == page }
                .map { it.first }

            for (j in i until update.size) {
                if (requiredPredecessors.contains(update[j])) {
                    return false
                }
            }
        }
        return true
    }

    private fun correctedOrder(update: List<Int>): List<Int> = buildList {
        val remainingUpdates = update.toMutableList()
        var requiredRules = rules
            .filter { update.contains(it.first) && update.contains(it.second) }

        while (remainingUpdates.isNotEmpty()) {
            val (firstIndex, firstPage) = remainingUpdates.withIndex()
                .firstOrNull { (_, page) -> !requiredRules.any { it.second == page } } ?: break
            add(firstPage)
            remainingUpdates.removeAt(firstIndex)
            requiredRules = requiredRules.filter { rule -> rule.first != firstPage }
        }
    }

    private fun parseInput() = readInput {
        val (rules, updates) = readLines().splitByBlank()

        Pair(
            rules.map { rule ->
                rule.split("|").let {
                    Pair(it[0].toInt(), it[1].toInt())
                }
            },
            updates.map { update ->
                update.split(",")
                    .map { it.toInt() }
            }
        )
    }
}
