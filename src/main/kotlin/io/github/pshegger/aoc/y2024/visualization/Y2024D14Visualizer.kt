package io.github.pshegger.aoc.y2024.visualization

import io.github.pshegger.aoc.common.model.Bitmap
import io.github.pshegger.aoc.common.model.Color
import io.github.pshegger.aoc.common.model.TaskVisualizer
import io.github.pshegger.aoc.y2024.Y2024D14

class Y2024D14Visualizer(private val robots: List<Y2024D14.Robot>, private val s: Int) : TaskVisualizer {

    override fun generateVisualization() {
        val bmp = Bitmap(
            Y2024D14.AREA_WIDTH * PIXEL_SIZE,
            Y2024D14.AREA_HEIGHT * PIXEL_SIZE,
            Color.Black,
        )

        robots.forEach {
            val pos = it.after(s).pos
            bmp.fillRect(Pair(pos.x * PIXEL_SIZE, pos.y * PIXEL_SIZE), PIXEL_SIZE, PIXEL_SIZE, Color(0x00ff00))
        }

        bmp.save("visualizations/Y2024D14.ppm")
    }

    companion object {
        private const val PIXEL_SIZE = 2
    }
}