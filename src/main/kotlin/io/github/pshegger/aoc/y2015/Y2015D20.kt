package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver
import kotlin.math.roundToInt
import kotlin.math.sqrt

class Y2015D20 : BaseSolver() {
    override val year = 2015
    override val day = 20

    override fun part1(): Int = generateSequence(1) { it + 1 }
        .first { it.getPresentCount(null, 10) >= INPUT }

    override fun part2(): Int = generateSequence(1) { it + 1 }
        .first { it.getPresentCount(50, 11) >= INPUT }

    private fun Int.getPresentCount(maxVisits: Int?, presentMultiplier: Int) = getDividers()
        .filter { maxVisits == null || (this / it <= maxVisits) }
        .sumOf { it * presentMultiplier }

    private fun Int.getDividers() = buildSet {
        val n = this@getDividers
        repeat(sqrt(n.toFloat()).roundToInt()) {
            if (n % (it + 1) == 0) {
                add(it + 1)
                add(n / (it + 1))
            }
        }
    }

    companion object {
        private const val INPUT = 29000000
    }
}