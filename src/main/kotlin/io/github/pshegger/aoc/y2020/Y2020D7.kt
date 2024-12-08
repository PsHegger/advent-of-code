package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2020D7 : BaseSolver() {
    override val year = 2020
    override val day = 7

    override fun part1(): Int = parseInput().let { rules ->
        rules.keys.count { color -> color.canContain(rules, "shiny gold") }
    }

    override fun part2(): Int = parseInput().bagCount("shiny gold")

    private fun String.canContain(rules: Map<String, List<Pair<Int, String>>>, bagColor: String): Boolean {
        val insideColors = (rules[this] ?: error("Color `$this` missing from rules")).map { it.second }
        if (bagColor in insideColors) {
            return true
        }
        return insideColors.any { color -> color.canContain(rules, bagColor) }
    }

    private fun Map<String, List<Pair<Int, String>>>.bagCount(containerColor: String): Int =
        (this[containerColor] ?: error("Color `$containerColor` missing from rules")).sumOf { (count, color) ->
            count * (bagCount(color) + 1)
        }

    private fun parseInput(): Map<String, List<Pair<Int, String>>> = readInput {
        readLines().associate { line ->
            val parts = line.split("contain")
            val color = parts[0].dropLast(6)
            if (parts[1].trim().startsWith("no ")) {
                Pair(color, emptyList())
            } else {
                val inside = parts[1].replace(".", "").split(",").map { p ->
                    val parts2 = p.trim().split(" ")
                    Pair(
                        parts2[0].toInt(),
                        parts2.slice(1..2).joinToString(" ")
                    )
                }
                Pair(color, inside)
            }
        }
    }
}
