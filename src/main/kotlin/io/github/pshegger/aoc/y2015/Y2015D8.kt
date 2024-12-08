package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2015D8 : BaseSolver() {
    override val year = 2015
    override val day = 8

    override fun part1(): Int = parseInput()
        .sumOf { it.length - it.lengthInMemory() }

    override fun part2(): Int = parseInput()
        .sumOf { it.encoded().length - it.length }

    private fun String.lengthInMemory() = this
        .replace("\\\\", "_")
        .replace("\\\"", "_")
        .replace("\"", "")
        .replace(Regex("\\\\x[a-fA-F0-9]{2}"), "_")
        .length

    private fun String.encoded() = this
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .let { "\"$it\"" }

    private fun parseInput() = readInput {
        readLines()
    }
}
