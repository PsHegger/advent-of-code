package io.github.pshegger.aoc.y2022

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2022D8 : BaseSolver() {
    override val year = 2022
    override val day = 8

    private val forest by lazy { parseInput() }
    private val transformedForest by lazy { forest.transform() }

    private val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

    override fun part1(): Int = transformedForest.flatten().count { it.second }
    override fun part2(): Int = transformedForest.flatten().maxOf { it.first }

    private fun List<List<Int>>.transform() = mapIndexed { y, row ->
        row.mapIndexed { x, n ->
            directions.map { (dx, dy) ->
                var seen = 0
                var nx = x + dx
                var ny = y + dy
                while (nx >= 0 && nx < row.size && ny >= 0 && ny < size) {
                    seen++
                    if (this[ny][nx] >= n) {
                        return@map Pair(seen, false)
                    }
                    nx += dx
                    ny += dy
                }
                Pair(seen, true)
            }.fold(Pair(1, false)) { result, (seen, canSee) ->
                Pair(seen * result.first, canSee || result.second)
            }
        }
    }

    private fun parseInput() = readInput {
        readLines().map { line ->
            line.map {
                it.digitToInt()
            }
        }
    }
}
