package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.extensions.extractAll
import io.github.pshegger.aoc.common.extensions.permutations
import io.github.pshegger.aoc.common.extensions.toExtractor

class Y2015D13 : BaseSolver() {
    override val year = 2015
    override val day = 13

    private val extractor = "%s would %s %d happiness units by sitting next to %s.".toExtractor()

    private val input by lazy { parseInput() }
    private val people by lazy { input.map { it.who }.toSet() }
    private val changes by lazy { input.associate { Pair("${it.who}:${it.neighbour}", it.change) } }

    override fun part1(): Int = people.solveForChanges(changes)

    override fun part2(): Any? {
        val peopleWithMe = people + MY_NAME
        val changesWithMe = changes + people.flatMap {
            listOf(
                Pair("$it:$MY_NAME", 0),
                Pair("$MY_NAME:$it", 0)
            )
        }

        return peopleWithMe.solveForChanges(changesWithMe)
    }

    private fun Set<String>.solveForChanges(changes: Map<String, Int>) =
        permutations().maxOf { sitting ->
            sitting.mapIndexed { i, who ->
                val n1 = if (i > 0) sitting[i - 1] else sitting.last()
                val n2 = if (i < sitting.size - 1) sitting[i + 1] else sitting.first()
                changes.getOrDefault("$who:$n1", 0) + changes.getOrDefault("$who:$n2", 0)
            }.sum()
        }

    private fun parseInput() = readInput {
        readLines()
            .extractAll(extractor) { (who, dir, amount, neighbor) ->
                val sign = if (dir == "gain") 1 else -1
                HappinessChanges(
                    who,
                    neighbor,
                    sign * amount.toInt()
                )
            }
    }

    private data class HappinessChanges(val who: String, val neighbour: String, val change: Int)

    companion object {

        private const val MY_NAME = "pshegger"
    }
}
