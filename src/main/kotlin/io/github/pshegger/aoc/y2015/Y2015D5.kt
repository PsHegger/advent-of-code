package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2015D5 : BaseSolver() {
    override val year = 2015
    override val day = 5

    private val rules1 = listOf<(String) -> Boolean>(
        { str -> str.count { it in "aeiou" } >= 3 },
        { str -> (0 until str.length - 1).any { str[it] == str[it + 1] } },
        { str -> listOf("ab", "cd", "pq", "xy").none { it in str } },
    )

    private val rules2 = listOf<(String) -> Boolean>(
        { str -> (0 until str.length - 1).any { str.substring(it, it + 2) in str.drop(it + 2) } },
        { str -> (0 until str.length - 2).any { str[it] == str[it + 2] } },
    )

    override fun part1(): Int =
        parseInput().count { it.isNice(rules1) }

    override fun part2(): Int =
        parseInput().count { it.isNice(rules2) }

    private fun String.isNice(rules: List<(String) -> Boolean>): Boolean =
        rules.all { it(this) }

    private fun parseInput() =
        readInput { readLines() }
}
