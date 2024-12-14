package io.github.pshegger.aoc.common.model

class Grid<T>(val width: Int, val height: Int, init: (Int) -> T) : Map<Coordinate, T> {

    private val fields: MutableList<T> = List(width * height, init).toMutableList()

    override operator fun get(key: Coordinate) = get(key.x, key.y)
    operator fun get(x: Int, y: Int) = fields[x + y * width]

    fun getOrNull(key: Coordinate) = getOrNull(key.x, key.y)
    fun getOrNull(x: Int, y: Int) = if (isInside(x, y)) get(x, y) else null

    operator fun set(c: Coordinate, value: T) = set(c.x, c.y, value)
    operator fun set(x: Int, y: Int, value: T) {
        fields[x + y * width] = value
    }

    fun isInside(c: Coordinate) = isInside(c.x, c.y)
    fun isInside(x: Int, y: Int) =
        x in 0 until width && y in 0 until height

    fun prettyPrint(mapper: (T) -> Char) = buildString {
        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                append(mapper(get(x, y)))
            }
            append("\n")
        }
    }

    override val entries: Set<Map.Entry<Coordinate, T>>
        get() = (0 until height).flatMap { y ->
            (0 until width).map { x ->
                Entry(Coordinate(x, y), get(x, y))
            }
        }.toSet()

    override val keys: Set<Coordinate>
        get() = (0 until height).flatMap { y ->
            (0 until width).map { x ->
                Coordinate(x, y)
            }
        }.toSet()

    override val size: Int
        get() = fields.size

    override val values: Collection<T>
        get() = fields

    override fun isEmpty(): Boolean = fields.isEmpty()
    override fun containsValue(value: T): Boolean = fields.contains(value)
    override fun containsKey(key: Coordinate): Boolean = isInside(key)

    data class Entry<K, V>(override val key: K, override val value: V) : Map.Entry<K, V>

    companion object {

        fun <T> fromLines(lines: List<String>, init: (Int) -> T, parser: (Char) -> T): Grid<T> {
            val height = lines.size
            val width = lines[0].length
            val grid = Grid(width, height, init)

            lines.forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    grid[x, y] = parser(c)
                }
            }

            return grid
        }
    }
}
