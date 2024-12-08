package io.github.pshegger.aoc.y2022

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.extensions.extractAll
import io.github.pshegger.aoc.common.extensions.splitByBlank
import io.github.pshegger.aoc.common.extensions.toExtractor

class Y2022D5 : BaseSolver() {
    override val year = 2022
    override val day = 5

    private val extractor = "move %d from %d to %d".toExtractor()

    override fun part1(): String = parseInput()
        .convertForPart1()
        .solve()

    override fun part2(): String = parseInput().solve()

    private fun Input.convertForPart1() = copy(
        steps = steps.flatMap { step -> List(step.count) { step.copy(count = 1) } }
    )

    private fun Input.solve() =
        steps.fold(starting) { stacks, step ->
            val moving = stacks[step.from].takeLast(step.count)
            stacks.mapIndexed { i, stack ->
                when (i) {
                    step.from -> stack.dropLast(step.count)
                    step.to -> stack + moving
                    else -> stack
                }
            }
        }
            .mapNotNull { it.lastOrNull() }
            .joinToString(separator = "")

    private fun parseInput() = readInput {
        val (starting, steps) = readLines().splitByBlank()
        val stackCount = starting.last()
            .split(" ")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .maxOf { it.toInt() }
        val stacks = List(stackCount) { mutableListOf<Char>() }
        starting.dropLast(1)
            .forEach { line ->
                line.chunked(4)
                    .map { it.trim() }
                    .forEachIndexed { i, item ->
                        if (item.length > 1 && !item[1].isWhitespace()) {
                            stacks[i].add(item[1])
                        }
                    }
            }
        Input(
            starting = stacks.map { it.reversed() },
            steps = steps.extractAll(extractor) { (count, from, to) ->
                Step(from.toInt() - 1, to.toInt() - 1, count.toInt())
            }
        )
    }

    private data class Step(val from: Int, val to: Int, val count: Int)
    private data class Input(val starting: List<List<Char>>, val steps: List<Step>)
}
