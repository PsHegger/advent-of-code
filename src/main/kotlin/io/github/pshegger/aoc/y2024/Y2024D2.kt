package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.model.BaseSolver
import kotlin.math.absoluteValue

class Y2024D2 : BaseSolver() {
    override val year = 2024
    override val day = 2

    override fun part1() = countSafe(parseInput(), false)
    override fun part2() = countSafe(parseInput(), true)

    private fun countSafe(reports: List<List<Int>>, allowDampening: Boolean) =
        reports.count { isReportSafe(it, allowDampening) }

    private fun isReportSafe(report: List<Int>, allowDampening: Boolean): Boolean {
        if (checkReport(report)) {
            return true
        } else if (!allowDampening) {
            return false
        }

        for (i in report.indices) {
            if (checkReport(report.take(i) + report.drop(i + 1))) {
                return true
            }
        }

        return false
    }

    private fun checkReport(report: List<Int>): Boolean {
        var isIncreasing: Boolean? = null
        for (i in 1 until report.size) {
            val diff = report[i] - report[i - 1]
            if (diff.absoluteValue !in 1..3) {
                return false
            }
            if (isIncreasing == null) {
                isIncreasing = diff > 0
            } else {
                if ((isIncreasing && diff < 0) || (!isIncreasing && diff > 0)) {
                    return false
                }
            }
        }
        return true
    }

    private fun parseInput() = readInput {
        readLines().map { line ->
            line.split(" ")
                .map { it.toInt() }
        }
    }
}
