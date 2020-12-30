package io.github.pshegger.aoc.y2020.visualization

import io.github.pshegger.aoc.common.Bitmap
import io.github.pshegger.aoc.common.Color
import io.github.pshegger.aoc.common.Coordinate
import io.github.pshegger.aoc.common.TaskVisualizer
import io.github.pshegger.aoc.y2020.Y2020D20

@ExperimentalStdlibApi
class Y2020D20Visualizer(private val finalImage: Y2020D20.Tile) : TaskVisualizer {
    override fun generateVisualization() {
        val monsterPixels = finalImage.seaMonsterCoords().flatMap { (x, y) ->
            Y2020D20.seaMonster.map { (dx, dy) -> Coordinate(x + dx, y + dy) }
        }

        val bmp = Bitmap(
            finalImage.fields[0].length * MAGNIFICATION,
            finalImage.fields.size * MAGNIFICATION,
            baseColor = waterColor
        )

        finalImage.fields.indices.forEach { y ->
            finalImage.fields[y].indices.forEach { x ->
                val chr = finalImage.fields[y][x]
                val topLeft = Pair(x * MAGNIFICATION, y * MAGNIFICATION)
                when {
                    Coordinate(x, y) in monsterPixels -> bmp.fillRect(topLeft, MAGNIFICATION, MAGNIFICATION, monsterColor)
                    chr == '#' -> bmp.fillRect(topLeft, MAGNIFICATION, MAGNIFICATION, obstacleColor)
                }
            }
        }

        bmp.save("visualizations/Y2020D20.ppm")
    }

    companion object {
        private val waterColor = Color(0x64b5f6)
        private val obstacleColor = Color(0x1976d2)
        private val monsterColor = Color(0xfdd835)

        private const val MAGNIFICATION = 5
    }
}
