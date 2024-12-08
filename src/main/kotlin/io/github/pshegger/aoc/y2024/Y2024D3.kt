package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.toExtractor

class Y2024D3 : BaseSolver() {
    override val year = 2024
    override val day = 3

    private val mulExtractor = "mul\\(%d,%d\\)".toExtractor()

    override fun part1() = calculateSum(false)
    override fun part2() = calculateSum(true)

    private fun calculateSum(areInstructionsEnabled: Boolean): Int {
        val re = "(mul\\(\\d{1,3},\\d{1,3}\\))|(do(?:n't)?)".toRegex()
        var sum = 0
        var isEnabled = true
        re.findAll(parseInput()).forEach {
            val group1 = it.groups[1]?.value
            val group2 = it.groups[2]?.value
            if (group1 != null) {
                if (!areInstructionsEnabled || isEnabled) {
                    sum += doMul(group1) ?: throw IllegalStateException("Cannot parse $group1 as a multiplication instruction")
                }
            } else if (group2 != null) {
                isEnabled = group2 == "do"
            }
        }
        return sum
    }

    private fun doMul(match: String) = mulExtractor.extract(match) { matches ->
        matches.map { it.toInt() }
            .fold(1) { acc, n -> acc * n }
    }

    private fun parseInput() = readInput {
        readLines().joinToString { it.trim() }
    }
}
