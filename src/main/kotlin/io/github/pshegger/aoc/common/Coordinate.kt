package io.github.pshegger.aoc.common

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
}
