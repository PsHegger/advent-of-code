package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.model.BaseSolver
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

class Y2024D7 : BaseSolver() {
    override val year = 2024
    override val day = 7

    override fun part1() = parseInput()
        .mapNotNull { checkCalibration(it, false) }
        .sum()

    override fun part2() = parseInput()
        .mapNotNull { checkCalibration(it, true) }
        .sum()

    private fun checkCalibration(
        calibration: Pair<Long, List<Long>>,
        allowConcat: Boolean,
        operators: List<Char> = emptyList(),
    ): Long? {
        val (result, ns) = calibration
        if (operators.size == ns.size - 1) {
            return if (eval(ns, operators) == result) {
                result
            } else {
                null
            }
        }

        val allowedOperations = if (allowConcat) "+*|" else "+*"
        for (operator in allowedOperations) {
            val res = checkCalibration(calibration, allowConcat, operators + operator)
            if (res != null) {
                return res
            }
        }

        return null
    }

    private fun eval(ns: List<Long>, operators: List<Char>): Long {
        val result = when (operators[0]) {
            '+' -> ns[0] + ns[1]
            '*' -> ns[0] * ns[1]
            '|' -> concat(ns[0], ns[1])
            else -> throw IllegalArgumentException("Unknown operator ${operators[0]}")
        }

        return if (operators.size == 1) {
            result
        } else {
            eval(listOf(result) + ns.drop(2), operators.drop(1))
        }
    }

    private fun concat(n1: Long, n2: Long): Long =
        n1 * (10f).pow(floor(log10(n2.toFloat())) + 1).roundToInt() + n2

    private fun parseInput() = readInput {
        readLines().map { line ->
            val (result, eq) = line.split(":")
            val ns = eq.split(" ").filterNot { it.isEmpty() }.map { it.toLong() }
            Pair(result.toLong(), ns)
        }
    }
}
