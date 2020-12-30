package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.Coordinate
import kotlin.math.max

class Y2015D6 : BaseSolver() {
    override val year = 2015
    override val day = 6

    override fun part1(): Int =
        parseInput().fold(List(GRID_SIZE * GRID_SIZE) { false }) { acc, instruction ->
            instruction.applyToState1(acc)
        }.count { it }

    override fun part2(): Int =
        parseInput().fold(List(GRID_SIZE * GRID_SIZE) { 0 }) { acc, instruction ->
            instruction.applyToState2(acc)
        }.sum()

    private fun parseInput() = readInput {
        readLines().map { Instruction.parseString(it) }
    }

    private data class Instruction(val type: InstructionType, val p1: Coordinate, val p2: Coordinate) {

        fun applyToState1(state: List<Boolean>) =
            getIndices().fold(state.toMutableList()) { acc, i ->
                acc[i] = when (type) {
                    InstructionType.On -> true
                    InstructionType.Off -> false
                    InstructionType.Toggle -> !acc[i]
                }
                acc
            }

        fun applyToState2(state: List<Int>) =
            getIndices().fold(state.toMutableList()) { acc, i ->
                acc[i] = when (type) {
                    InstructionType.On -> acc[i] + 1
                    InstructionType.Off -> max(0, acc[i] - 1)
                    InstructionType.Toggle -> acc[i] + 2
                }
                acc
            }

        private fun getIndices(): List<Int> = (p1.x..p2.x)
            .flatMap { x ->
                (p1.y..p2.y).map { y ->
                    y * GRID_SIZE + x
                }
            }

        companion object {
            fun parseString(str: String): Instruction {
                val type = InstructionType.values().first { it.regex.matches(str) }
                val groups = type.regex.matchEntire(str)?.groupValues ?: error("That should never happen")
                return Instruction(
                    type,
                    Coordinate(groups[1].toInt(), groups[2].toInt()),
                    Coordinate(groups[3].toInt(), groups[4].toInt()),
                )
            }
        }
    }

    private enum class InstructionType(val regex: Regex) {
        On(turnOnRegex),
        Off(turnOffRegex),
        Toggle(toggleRegex)
    }

    companion object {
        private val turnOnRegex = "turn on (\\d+),(\\d+) through (\\d+),(\\d+)".toRegex()
        private val turnOffRegex = "turn off (\\d+),(\\d+) through (\\d+),(\\d+)".toRegex()
        private val toggleRegex = "toggle (\\d+),(\\d+) through (\\d+),(\\d+)".toRegex()

        private const val GRID_SIZE = 1000
    }
}
