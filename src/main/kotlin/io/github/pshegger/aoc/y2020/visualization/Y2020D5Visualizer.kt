package io.github.pshegger.aoc.y2020.visualization

import io.github.pshegger.aoc.common.Bitmap
import io.github.pshegger.aoc.common.Color
import io.github.pshegger.aoc.common.Coordinate
import io.github.pshegger.aoc.common.TaskVisualizer

class Y2020D5Visualizer(private val seats: List<Coordinate>) : TaskVisualizer {

    override fun generateVisualization() {
        if (!IS_ENABLED) return

        val bmp = Bitmap(1030, 84, Color(0x757575))

        (0 until 128).forEach { row ->
            (0 until 8).forEach { seat ->
                bmp.fillRect(Coordinate(row, seat).getCoords(), 5, 5, unoccupiedColor)
            }
        }

        bmp.save("visualizations/y2020d5/00000.ppm")

        seats.forEachIndexed { index, seat ->
            bmp.fillRect(seat.getCoords(), 5, 5, occupiedColor)
            bmp.save("visualizations/y2020d5/${String.format("%05d", index + 1)}.ppm")
        }
    }

    private fun Coordinate.getCoords() = Pair(
        x * 8 + 5,
        when (y) {
            in 0..1 -> y * 7 + 5
            in 2..5 -> y * 7 + 15
            else -> y * 7 + 25
        }
    )

    companion object {
        private const val IS_ENABLED = false

        private val unoccupiedColor = Color(0xCFD8DC)
        private val occupiedColor = Color(0xEF5350)
    }
}
