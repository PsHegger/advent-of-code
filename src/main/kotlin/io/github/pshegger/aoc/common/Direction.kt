package io.github.pshegger.aoc.common

enum class Direction {
    North, East, South, West;

    fun asCoordinate() = when (this) {
        North -> Coordinate(0, -1)
        East -> Coordinate(1, 0)
        South -> Coordinate(0, 1)
        West -> Coordinate(-1, 0)
    }

    fun turnLeft() = when (this) {
        North -> West
        East -> North
        South -> East
        West -> South
    }

    fun turnRight() = when (this) {
        North -> East
        East -> South
        South -> West
        West -> North
    }
}
