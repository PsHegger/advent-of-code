package io.github.pshegger.aoc.y2022

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.extensions.findCommons

class Y2022D3 : BaseSolver() {
    override val year = 2022
    override val day = 3

    override fun part1(): Int = parseInput()
        .map { line ->
            val halfCount = line.length / 2
            listOf(line.take(halfCount), line.drop(halfCount))
                .map { it.toList() }
                .findCommons()
                .first()
        }
        .sumOf { it.toPriority() }

    override fun part2(): Int = parseInput()
        .chunked(3)
        .map { group ->
            group.map { it.toList() }
                .findCommons()
                .first()
        }
        .sumOf { it.toPriority() }

    private fun Char.toPriority() = if (this in 'a'..'z') {
        this - 'a' + 1
    } else {
        this - 'A' + 27
    }

    private fun parseInput() = readInput {
        readLines()
    }
}
