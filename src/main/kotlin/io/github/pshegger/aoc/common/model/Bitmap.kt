package io.github.pshegger.aoc.common.model

import io.github.pshegger.aoc.common.utils.getCartesianProduct
import java.io.File
import kotlin.math.max
import kotlin.math.min

class Bitmap(private val width: Int, private val height: Int, private val baseColor: Color = Color.White) {
    private val pixels = MutableList(width * height) { baseColor }

    fun setColor(x: Int, y: Int, color: Color) {
        if (x < 0 || x >= width || y < 0 || y >= height) return
        pixels[y * width + x] = color
    }

    fun drawRect(topLeft: Pair<Int, Int>, width: Int, height: Int, color: Color) {
        val (x, y) = topLeft
        (0 until width).forEach { dx ->
            setColor(x + dx, y, color)
            setColor(x + dx, y + height - 1, color)
        }
        (0 until height).forEach { dy ->
            setColor(x, y + dy, color)
            setColor(x + width - 1, y + dy, color)
        }
    }

    fun fillRect(topLeft: Pair<Int, Int>, width: Int, height: Int, color: Color) {
        val (x, y) = topLeft
        (0 until height).forEach { dy ->
            (0 until width).forEach { dx ->
                setColor(x + dx, y + dy, color)
            }
        }
    }

    fun drawCircle(center: Pair<Int, Int>, radius: Int, color: Color) = drawCircleInternal(center, radius, color, false)
    fun fillCircle(center: Pair<Int, Int>, radius: Int, color: Color) = drawCircleInternal(center, radius, color, true)

    fun save(fileName: String) = File(fileName).printWriter().use { writer ->
        writer.println("P3")
        writer.println("$width $height")
        writer.println("255")
        pixels.forEach { writer.println(it.toPPMPixel()) }
    }

    private fun drawCircleInternal(center: Pair<Int, Int>, radius: Int, color: Color, fill: Boolean) {
        val (cx, cy) = center
        val rows = (cy - radius..cy + radius).associateWith { Pair(Int.MAX_VALUE, Int.MIN_VALUE) }.toMutableMap()

        fun updateRows(x: Int, y: Int) {
            val minX = min(x, rows[y]!!.first)
            val maxX = max(x, rows[y]!!.second)
            rows[y] = Pair(minX, maxX)
        }

        fun putPixels(cx: Int, cy: Int, x: Int, y: Int) {
            foo.forEach { (dx, dy) ->
                setColor(cx + dx * x, cy + dy * y, color)
                setColor(cx + dx * y, cy + dy * x, color)
                if (fill) {
                    updateRows(cx + dx * x, cy + dy * y)
                    updateRows(cx + dx * y, cy + dy * x)
                }
            }
        }

        var x = 0
        var y = radius
        var d = 3 - 2 * radius
        putPixels(cx, cy, x, y)
        while (y >= x) {
            x++
            d += if (d > 0) {
                y--
                4 * (x - y) + 10
            } else {
                4 * x + 6
            }
            putPixels(cx, cy, x, y)
        }

        if (fill) {
            rows.forEach { rowY, (minX, maxX) ->
                (minX..maxX).forEach { x -> setColor(x, rowY, color) }
            }
        }
    }

    companion object {
        private val foo = listOf(setOf(-1, 1), setOf(-1, 1)).getCartesianProduct()
    }
}
