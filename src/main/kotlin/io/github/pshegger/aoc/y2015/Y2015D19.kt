package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.extensions.splitByBlank
import io.github.pshegger.aoc.common.extensions.toExtractor
import io.github.pshegger.aoc.common.model.BaseSolver

class Y2015D19 : BaseSolver() {
    override val year = 2015
    override val day = 19

    private val input by lazy { parseInput() }
    private val molecule by lazy { input.molecule }
    private val replacements by lazy { input.replacements.sortedByDescending { it.second.length } }

    override fun part1(): Int = replacements
        .flatMap { getAllPossible(molecule, it) }
        .toSet()
        .size

    override fun part2(): Int {
        var current = molecule
        var steps = 0
        while (current != "e") {
            val replacement = replacements.first { it.second in current }
            current = current.replaceFirst(replacement.second, replacement.first)
            steps++
        }
        return steps
    }

    private fun getAllPossible(starter: String, replacement: Pair<String, String>) = buildSet {
        var startIndex = starter.indexOf(replacement.first)
        while (startIndex >= 0) {
            add(starter.take(startIndex) + replacement.second + starter.drop(startIndex + replacement.first.length))
            startIndex = starter.indexOf(replacement.first, startIndex + 1)
        }
    }

    private fun parseInput() = readInput {
        val replacementExtractor = "%s => %s".toExtractor()
        val (replacementLines, starter) = readLines().splitByBlank()

        val replacements = replacementLines.mapNotNull { line ->
            replacementExtractor.extract(line) { Pair(it[0], it[1]) }
        }

        Input(starter.first(), replacements)
    }

    private data class Input(val molecule: String, val replacements: List<Pair<String, String>>)
}