package io.github.pshegger.aoc.common

class Grid<T>(val width: Int, val height: Int, init: (Int) -> T) {

    private val fields: MutableList<T> = List(width * height, init).toMutableList()

    operator fun get(c: Coordinate) = get(c.x, c.y)
    operator fun get(x: Int, y: Int) = fields[x + y * width]

    operator fun set(c: Coordinate, value: T) = set(c.x, c.y, value)
    operator fun set(x: Int, y: Int, value: T) {
        fields[x + y * width] = value
    }

    fun isInside(c: Coordinate) = isInside(c.x, c.y)
    fun isInside(x: Int, y: Int) =
        x in 0 until width && y in 0 until height
}
