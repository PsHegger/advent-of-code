package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.BaseSolver

class Y2020D19 : BaseSolver() {
    override val year = 2020
    override val day = 19

    override fun part1(): Int {
        val (rules, messages) = parseInput()
        val validRegex = "^${calculateRule(rules, false)}$".toRegex()
        return messages.count { validRegex.matches(it) }
    }

    override fun part2(): Int {
        val (rules, messages) = parseInput()
        val validRegex = "^${calculateRule(rules, true)}$".toRegex()
        return messages.count { validRegex.matches(it) }
    }

    private fun calculateRule(rules: Map<Int, List<RuleMatch>>, p2: Boolean): String {
        fun Int.regex(depth: Int = 0): String = when {
            p2 && this == 8 -> "(?:${42.regex()})+"
            p2 && this == 11 && depth > 10 -> ""
            p2 && this == 11 -> "(?:${42.regex()}${11.regex(depth + 1)}?${31.regex()})"
            else -> (rules[this] ?: error("Missing rule $this"))
                .joinToString("|", prefix = "(?:", postfix = ")") {
                    when (it) {
                        is RuleMatch.Data -> it.str
                        is RuleMatch.OtherRules -> it.ruleIds.joinToString("") { id -> id.regex() }
                    }
                }
        }

        return 0.regex()
    }

    private fun parseInput() = readInput {
        val lines = readLines()
        val ruleLines = lines.takeWhile { it.isNotBlank() }
        val messages = lines.dropWhile { it.isNotBlank() }.drop(1)
        Pair(parseRules(ruleLines), messages)
    }

    private fun parseRules(lines: List<String>): Map<Int, List<RuleMatch>> =
        lines.associate { line ->
            val (idStr, matchersStr) = line.split(":", limit = 2)
            val matchers = matchersStr
                .split("|")
                .map { matcher ->
                    val dataMatch = "\"(.+)\"".toRegex().matchEntire(matcher.trim())
                    if (dataMatch != null) {
                        RuleMatch.Data(dataMatch.groupValues[1])
                    } else {
                        RuleMatch.OtherRules(
                            matcher.trim().split(" ").map { it.toInt() }
                        )
                    }
                }
            val id = idStr.toInt()
            Pair(id, matchers)
        }

    private sealed class RuleMatch {
        data class OtherRules(val ruleIds: List<Int>) : RuleMatch()
        data class Data(val str: String) : RuleMatch()
    }
}
