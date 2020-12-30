package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.Coordinate
import io.github.pshegger.aoc.common.TaskVisualizer
import io.github.pshegger.aoc.y2020.visualization.Y2020D5Visualizer

class Y2020D5 : BaseSolver() {
    override val year = 2020
    override val day = 5

    override fun getVisualizer(): TaskVisualizer = Y2020D5Visualizer(parseInput())

    override fun part1(): Int =
        parseInput().maxOf { it.toSeatId() }

    override fun part2(): Int =
        parseInput().map { it.toSeatId() }.sorted().let { seatIds ->
            val prevSeatIndex = (0 until seatIds.size - 1).first { i ->
                seatIds[i] + 2 == seatIds[i + 1]
            }
            seatIds[prevSeatIndex] + 1
        }

    private fun Coordinate.toSeatId() = x * 8 + y

    private fun parseInput() = readInput {
        readLines().map { line ->
            val row = line.take(7).replace('F', '0').replace('B', '1').toInt(2)
            val seat = line.drop(7).replace('L', '0').replace('R', '1').toInt(2)
            Coordinate(row, seat)
        }
    }
}
