package io.github.pshegger.aoc.common.model

import io.github.pshegger.aoc.common.utils.getCartesianProduct

data class Coordinate(val x: Int, val y: Int) {

    val adjacents: List<Coordinate> by lazy {
        listOf((-1..1).toSet(), (-1..1).toSet()).getCartesianProduct()
            .mapNotNull { d ->
                if (d.all { it == 0 }) {
                    null
                } else {
                    Coordinate(x + d[0], y + d[1])
                }
            }
    }

    val cardinalNeighbours: List<Coordinate> by lazy {
        Direction.entries.map { this + it.asCoordinate() }
    }

    operator fun plus(o: Coordinate) = Coordinate(
        x = x + o.x,
        y = y + o.y,
    )

    operator fun minus(o: Coordinate) = Coordinate(
        x = x - o.x,
        y = y - o.y,
    )
}
