package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.extensions.toExtractor
import io.github.pshegger.aoc.common.extensions.updated
import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.model.Coordinate
import io.github.pshegger.aoc.common.model.Grid
import io.github.pshegger.aoc.common.model.TaskVisualizer
import io.github.pshegger.aoc.y2024.visualization.Y2024D14Visualizer

class Y2024D14 : BaseSolver() {
    override val year = 2024
    override val day = 14

    private val robots by lazy { parseInput() }
    private val part2Solution by lazy {
        generateSequence(0) { it + 1 }
            .first { quadrantCountsAfter(it).doesAnyContainMost }
            .also { if (PRINT_PART2) printAreaAfter(it) }
    }

    override fun getVisualizer(): TaskVisualizer = Y2024D14Visualizer(robots, part2Solution)

    override fun part1(): Int = quadrantCountsAfter(100).fold(1) { acc, n -> acc * n }
    override fun part2(): Int = part2Solution

    private fun quadrantCountsAfter(s: Int) = robots
        .fold(listOf(0, 0, 0, 0)) { counts, robot ->
            robot.after(s).pos.quadrantIndex?.let {
                counts.updated(it, counts[it] + 1)
            } ?: counts
        }

    private val List<Int>.doesAnyContainMost: Boolean
        get() = zip(indices)
            .any { (count, i) ->
                count > filterIndexed { index, _ -> index != i }.sum()
            }

    private val Coordinate.quadrantIndex: Int?
        get() {
            val qY = if (y < AREA_HEIGHT / 2) {
                0
            } else if (y > AREA_HEIGHT / 2) {
                1
            } else {
                return null
            }
            val qX = if (x < AREA_WIDTH / 2) {
                0
            } else if (x > AREA_WIDTH / 2) {
                1
            } else {
                return null
            }
            return 2 * qY + qX
        }

    private fun parseInput() = readInput {
        val extractor = "p=%d,%d v=%d,%d".toExtractor()
        readLines().mapNotNull { line ->
            extractor.extract(line) { found ->
                val coords = found.map { it.toInt() }
                Robot(Coordinate(coords[0], coords[1]), Coordinate(coords[2], coords[3]))
            }
        }
    }

    private fun printAreaAfter(s: Int) {
        val g = Grid(AREA_WIDTH, AREA_HEIGHT) { false }
        robots.forEach { g[it.after(s).pos] = true }
        println(g.prettyPrint { if (it) '#' else ' ' })
    }

    data class Robot(var pos: Coordinate, val v: Coordinate) {

        fun after(s: Int): Robot {
            var newX = (pos.x + s * v.x) % AREA_WIDTH
            if (newX < 0) newX += AREA_WIDTH
            var newY = (pos.y + s * v.y) % AREA_HEIGHT
            if (newY < 0) newY += AREA_HEIGHT
            return copy(pos = Coordinate(newX, newY))
        }
    }

    companion object {
        const val AREA_WIDTH = 101
        const val AREA_HEIGHT = 103
        private const val PRINT_PART2 = false
    }
}