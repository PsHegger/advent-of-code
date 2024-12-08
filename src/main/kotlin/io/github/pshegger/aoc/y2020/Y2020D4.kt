package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.model.TaskVisualizer
import io.github.pshegger.aoc.common.extensions.splitByBlank
import io.github.pshegger.aoc.y2020.visualization.Y2020D4Visualizer
import kotlin.math.roundToInt

class Y2020D4 : BaseSolver() {
    override val year = 2020
    override val day = 4

    private val validators = mapOf(
        "byr" to { it.toInt() in 1920..2002 },
        "iyr" to { it.toInt() in 2010..2020 },
        "eyr" to { it.toInt() in 2020..2030 },
        "hgt" to ::isValidHeight,
        "hcl" to { hairColorRegex.matches(it) },
        "ecl" to { it in eyeColors },
        "pid" to { passportIdRegex.matches(it) },
    )

    override fun getVisualizer(): TaskVisualizer = Y2020D4Visualizer(parseInput().filter { it.isValid(validators) })

    override fun part1(): Int =
        parseInput().count { it.hasAllRequiredFields(validators.keys) }

    override fun part2(): Int =
        parseInput().count { it.isValid(validators) }

    private fun isValidHeight(height: String): Boolean {
        val result = heightRegex.matchEntire(height) ?: return false
        val unit = result.groupValues[2]
        val value = result.groupValues[1].toInt()
        return (unit == "cm" && value in 150..193) || (unit == "in" && value in 59..76)
    }

    private fun parseInput() = readInput {
        readLines().splitByBlank().map { Passport.parseLines(it) }
    }

    data class Passport(val fields: Map<String, String>) {

        val heightCm: Int?
            get() {
                val result = heightRegex.matchEntire(fields["hgt"] ?: "") ?: return null
                val unit = result.groupValues[2]
                val value = result.groupValues[1].toInt()
                return if (unit == "cm") {
                    value
                } else {
                    (value * 2.54).roundToInt()
                }
            }

        fun hasAllRequiredFields(requiredFields: Set<String>) =
            requiredFields.all { it in fields }

        fun isValid(validators: Map<String, (String) -> Boolean>) =
            validators.all { (fieldName, validator) ->
                val value = fields[fieldName] ?: return@all false
                validator(value)
            }

        companion object {
            fun parseLines(lines: List<String>): Passport =
                lines.flatMap { it.split(" ") }
                    .associate { field ->
                        val parts = field.split(":")
                        Pair(parts[0], parts[1].trim())
                    }
                    .let { Passport(it) }
        }
    }

    companion object {
        private val heightRegex = "^([0-9]+)(cm|in)$".toRegex()
        private val hairColorRegex = "^#[0-9a-f]{6}$".toRegex()
        private val passportIdRegex = "^[0-9]{9}$".toRegex()
        private val eyeColors = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
    }
}
