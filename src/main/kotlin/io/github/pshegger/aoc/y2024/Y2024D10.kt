package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.model.Coordinate
import io.github.pshegger.aoc.common.model.Direction
import io.github.pshegger.aoc.common.model.Grid

class Y2024D10 : BaseSolver() {
    override val year = 2024
    override val day = 10

    private val input by lazy { parseInput() }

    override fun part1(): Int = input.entries
        .filter { (_, height) -> height == 0 }
        .sumOf { (pos, _) -> getReachableSummits(pos).size }

    override fun part2(): Int = input.entries
        .filter { (_, height) -> height == 0 }
        .sumOf { (pos, _) -> calculateScore(pos) }

    private fun getReachableSummits(pos: Coordinate): Set<Coordinate> = when (val height = input[pos]) {
        9 -> setOf(pos)
        else -> Direction.entries
            .asSequence()
            .map { pos + it.asCoordinate() }
            .filter { input.isInside(it) && input[it] == height + 1 }
            .flatMap { getReachableSummits(it) }
            .toSet()
    }

    private fun calculateScore(pos: Coordinate): Int = when (val height = input[pos]) {
        9 -> 1
        else -> Direction.entries
            .asSequence()
            .map { pos + it.asCoordinate() }
            .filter { input.isInside(it) && input[it] == height + 1 }
            .sumOf { calculateScore(it) }
    }

    private fun parseInput() = readInput {
        Grid.fromLines(readLines(), { 0 }) { it.digitToInt() }
    }
}