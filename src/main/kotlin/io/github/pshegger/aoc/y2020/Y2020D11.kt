package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.Coordinate

class Y2020D11 : BaseSolver() {
    override val year = 2020
    override val day = 11

    override fun part1(): Int = calculateOccupiedSeats(true, 4)
    override fun part2(): Int = calculateOccupiedSeats(false, 5)

    private fun calculateOccupiedSeats(adjacentMode: Boolean, emptyThreshold: Int): Int {
        var prev = parseInput()
        val vision = prev.calculateVision(adjacentMode)
        var settled = false
        while (!settled) {
            val n = prev.step(vision, emptyThreshold)
            if (n == prev) {
                settled = true
            }
            prev = n
        }
        return prev.sumBy { row -> row.count { it == '#' } }
    }

    private fun List<String>.calculateVision(adjacentMode: Boolean): Map<Coordinate, List<Coordinate>> =
        flatMapIndexed { y, row ->
            row.mapIndexed { x, _ ->
                val c = Coordinate(x, y)
                Pair(c, calculateVisionAt(c, adjacentMode))
            }
        }.toMap()

    private fun List<String>.calculateVisionAt(c: Coordinate, adjacentMode: Boolean): List<Coordinate> =
        (-1..1).flatMap { dy ->
            (-1..1).mapNotNull { dx ->
                when {
                    dx == 0 && dy == 0 -> null
                    adjacentMode && isSeat(Coordinate(c.x + dx, c.y + dy)) -> Coordinate(c.x + dx, c.y + dy)
                    !adjacentMode -> {
                        var seatFound = false
                        var seat: Coordinate? = null
                        var i = 1
                        while (!seatFound) {
                            val dc = Coordinate(c.x + dx * i, c.y + dy * i)
                            if (!isInBounds(dc)) {
                                seatFound = true
                            } else if (isSeat(dc)) {
                                seatFound = true
                                seat = dc
                            }
                            i++
                        }
                        seat
                    }
                    else -> null
                }
            }
        }

    private fun List<String>.isInBounds(c: Coordinate) =
        c.y in indices && c.x in this[c.y].indices

    private fun List<String>.isSeat(c: Coordinate) =
        isInBounds(c) && this[c.y][c.x] in listOf('L', '#')

    private fun List<String>.step(
        vision: Map<Coordinate, List<Coordinate>>,
        emptyThreshold: Int
    ): List<String> =
        mapIndexed { y, row ->
            row.mapIndexed { x, c ->
                val occupiedAdjacents = (vision[Coordinate(x, y)] ?: error("No precomputed vision found for ($x, $y)"))
                    .count { (ax, ay) -> this[ay][ax] == '#' }
                when {
                    c == 'L' && occupiedAdjacents == 0 -> '#'
                    c == '#' && occupiedAdjacents >= emptyThreshold -> 'L'
                    else -> c
                }
            }.joinToString("")
        }

    private fun parseInput() = readInput { readLines() }
}
