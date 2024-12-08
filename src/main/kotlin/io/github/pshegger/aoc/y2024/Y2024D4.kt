package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.Coordinate

class Y2024D4 : BaseSolver() {
    override val year = 2024
    override val day = 4

    private val input: List<String> by lazy { parseInput() }

    override fun part1() = solveWith(::checkPosition1)
    override fun part2() = solveWith(::checkPosition2)

    private fun solveWith(checker: (Coordinate) -> Int) = input.indices.sumOf { y ->
        input[y].indices.sumOf { x ->
            checker(Coordinate(x, y))
        }
    }

    private fun checkPosition1(coordinate: Coordinate): Int = listOf(
        Pair(1, -1), Pair(1, 0), Pair(1, 1), Pair(0, 1)
    ).count { dir ->
        val sub = substring(coordinate, TARGET_1.length, dir)
        sub == TARGET_1 || sub == TARGET_1_REVERSED
    }

    private fun checkPosition2(coordinate: Coordinate): Int {
        val isCorrect = listOf(
            Pair(-1, -1), Pair(-1, 1)
        ).all { startDelta ->
            val start = Coordinate(
                x = coordinate.x + startDelta.first,
                y = coordinate.y + startDelta.second,
            )
            val dir = Pair(startDelta.first * -1, startDelta.second * -1)
            val sub = substring(start, TARGET_2.length, dir)
            sub == TARGET_2 || sub == TARGET_2_REVERSED
        }

        return if (isCorrect) 1 else 0
    }

    private fun get(x: Int, y: Int): Char? {
        if (y !in input.indices) {
            return null
        }
        val line = input[y]
        if (x !in line.indices) {
            return null
        }

        return line[x]
    }

    private fun substring(start: Coordinate, length: Int, direction: Pair<Int, Int>): String? = buildString {
        repeat(length) { i ->
            get(
                x = start.x + i * direction.first,
                y = start.y + i * direction.second,
            )?.let { append(it) } ?: return null
        }
    }

    private fun parseInput() = readInput {
        readLines().filterNot { it.isEmpty() }
    }

    companion object {
        private const val TARGET_1 = "XMAS"
        private val TARGET_1_REVERSED = TARGET_1.reversed()

        private const val TARGET_2 = "MAS"
        private val TARGET_2_REVERSED = TARGET_2.reversed()
    }
}
