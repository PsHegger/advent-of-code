package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2020D2 : BaseSolver() {
    override val year = 2020
    override val day = 2

    override fun part1(): Int = parseInput().count { it.isValid1 }
    override fun part2(): Int = parseInput().count { it.isValid2 }

    private fun parseInput() = readInput {
        readLines().map { PasswordPolicy.parseString(it) }
    }

    private data class PasswordPolicy(val n0: Int, val n1: Int, val chr: Char, val passwd: String) {

        val isValid1: Boolean get() = passwd.count { it == chr } in n0..n1
        val isValid2: Boolean get() = (passwd[n0 - 1] == chr) xor (passwd[n1 - 1] == chr)

        companion object {
            private val ruleRegex = "(\\d+)-(\\d+) (\\w): (.+)".toRegex()

            fun parseString(str: String): PasswordPolicy {
                val groups = ruleRegex.matchEntire(str)?.groupValues ?: error("Line doesn't match format")
                return PasswordPolicy(
                    groups[1].toInt(),
                    groups[2].toInt(),
                    groups[3][0],
                    groups[4],
                )
            }
        }
    }
}
