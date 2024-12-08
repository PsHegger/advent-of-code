package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2015D2 : BaseSolver() {
    override val year = 2015
    override val day = 2

    override fun part1(): Int =
        parseInput().map { (l, w, h) ->
            val faces = listOf(
                l * w, w * h, h * l
            )
            2 * faces.sum() + faces.minOf { it }
        }.sum()

    override fun part2(): Int =
        parseInput().map { (l, w, h) ->
            val distances = listOf(
                2 * (w + l), 2 * (h + l), 2 * (w + h)
            )
            distances.minOf { it } + l * w * h
        }.sum()

    private fun parseInput(): List<BoxSize> =
        readInput {
            readLines().map { line ->
                val (length, width, height) = line.split("x", limit = 3).map { it.toInt() }
                BoxSize(length, width, height)
            }
        }

    private data class BoxSize(val length: Int, val width: Int, val height: Int)
}
