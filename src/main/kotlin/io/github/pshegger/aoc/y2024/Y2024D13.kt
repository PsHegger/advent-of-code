package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.extensions.splitByBlank
import io.github.pshegger.aoc.common.extensions.toExtractor
import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.model.Coordinate
import io.github.pshegger.aoc.common.utils.RegExtractor
import kotlin.math.min

class Y2024D13 : BaseSolver() {
    override val year = 2024
    override val day = 13

    private val machines by lazy { parseInput() }

    override fun part1() = machines.mapNotNull { it.calculateMinToken(false) }.sum()
    override fun part2() = machines.mapNotNull { it.calculateMinToken(true) }.sum()

    private fun parseInput() = readInput {
        val buttonExtractor = "Button %s: X+%d, Y+%d".toExtractor()
        val prizeExtractor = "Prize: X=%d, Y=%d".toExtractor()

        fun String.extractCoordinates(extractor: RegExtractor, firstIndex: Int) = extractor.extract(this) {
            Coordinate(it[firstIndex].toInt(), it[firstIndex + 1].toInt())
        } ?: error("Cannot parse `$this`")

        readLines()
            .splitByBlank()
            .map { machine ->
                val buttonA = machine[0].extractCoordinates(buttonExtractor, 1)
                val buttonB = machine[1].extractCoordinates(buttonExtractor, 1)
                val prize = machine[2].extractCoordinates(prizeExtractor, 0)

                ClawMachine(buttonA, buttonB, prize)
            }
    }

    private data class ClawMachine(val buttonA: Coordinate, val buttonB: Coordinate, val prize: Coordinate) {

        val correctedPrizeX = prize.x + 10000000000000
        val correctedPrizeY = prize.y + 10000000000000

        fun calculateMinToken(isPart2: Boolean): Long? {
            val (prizeX, prizeY) = if (isPart2) {
                Pair(correctedPrizeX, correctedPrizeY)
            } else {
                Pair(prize.x.toLong(), prize.y.toLong())
            }
            val b = (buttonA.x * prizeY - buttonA.y * prizeX) / (buttonA.x * buttonB.y - buttonA.y * buttonB.x)
            val a = (prizeX - b * buttonB.x) / buttonA.x
            val finalX = a * buttonA.x + b * buttonB.x
            val finalY = a * buttonA.y + b * buttonB.y

            return if (finalX == prizeX && finalY == prizeY) {
                3 * a + b
            } else {
                null
            }
        }
    }
}