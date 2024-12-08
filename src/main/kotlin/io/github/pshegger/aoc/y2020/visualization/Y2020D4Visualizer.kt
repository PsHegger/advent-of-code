package io.github.pshegger.aoc.y2020.visualization

import io.github.pshegger.aoc.common.model.Bitmap
import io.github.pshegger.aoc.common.model.Color
import io.github.pshegger.aoc.common.model.TaskVisualizer
import io.github.pshegger.aoc.y2020.Y2020D4
import kotlin.math.min

class Y2020D4Visualizer(private val passports: List<Y2020D4.Passport>) : TaskVisualizer {

    override fun generateVisualization() {
        val bmp = Bitmap(594, 594, Color(0x6495ED))
        (1..10).forEach { bmp.fillRect(Pair(0, it * CELL_HEIGHT), 594, 1, Color.Black) }
        (1..17).forEach { bmp.fillRect(Pair(it * CELL_WIDTH, 0), 1, 594, Color.Black) }
        passports.indices.forEach { drawImage(bmp, it) }
        bmp.save("visualizations/Y2020D4.ppm")
    }

    private fun drawImage(bmp: Bitmap, index: Int) {
        val hairColorStr = passports[index].fields["hcl"] ?: return
        val eyeColorStr = passports[index].fields["ecl"] ?: return

        val heightCm = passports[index].heightCm ?: return
        val hairColor = Color.parseString(hairColorStr) ?: return
        val eyeColor = when (eyeColorStr) {
            "oth" -> Color.random()
            else -> eyeColors[eyeColorStr] ?: return
        }
        val skinColor = skinColors.random()
        val shirtColor = Color.random()

        val x = index % HORIZONTAL_COUNT
        val y = (index - x) / HORIZONTAL_COUNT
        val dx = x * CELL_WIDTH
        val dy = y * CELL_HEIGHT

        val headTop = 6 - ((heightCm - 193) / 2)

        // draw hair
        bmp.fillRect(Pair(10 + dx, headTop + dy - 3), 13, 1, hairColor)
        bmp.fillRect(Pair(9 + dx, headTop + dy - 2), 15, 1, hairColor)
        bmp.fillRect(Pair(8 + dx, headTop + dy - 1), 17, 10, hairColor)

        // draw head
        bmp.fillRect(Pair(9 + dx, headTop + dy), 15, 18, skinColor)
        bmp.drawRect(Pair(9 + dx, headTop + dy), 15, 18, Color.Black)

        // draw hair 2
        bmp.fillRect(Pair(9 + dx, headTop + dy), 15, 2, hairColor)

        // draw eyes
        bmp.fillRect(Pair(12 + dx, headTop + dy + 4), 4, 4, eyeColor)
        bmp.drawRect(Pair(12 + dx, headTop + dy + 4), 4, 4, Color.Black)
        bmp.fillRect(Pair(17 + dx, headTop + dy + 4), 4, 4, eyeColor)
        bmp.drawRect(Pair(17 + dx, headTop + dy + 4), 4, 4, Color.Black)

        // draw mouth
        bmp.fillRect(Pair(14 + dx, headTop + dy + 13), 5, 1, Color.Black)
        bmp.fillRect(Pair(12 + dx, headTop + dy + 12), 2, 1, Color.Black)
        bmp.fillRect(Pair(19 + dx, headTop + dy + 12), 2, 1, Color.Black)

        // draw neck
        bmp.fillRect(Pair(13 + dx, headTop + dy + 17), 7, 7, skinColor)
        bmp.drawRect(Pair(13 + dx, headTop + dy + 17), 7, 7, Color.Black)

        // draw body
        val bodyTop = headTop + dy + 23
        val bodyHeight = min(bodyTop + 30, dy + CELL_HEIGHT + 1) - bodyTop
        bmp.fillRect(Pair(3 + dx, bodyTop), 28, bodyHeight, shirtColor)
        bmp.drawRect(Pair(3 + dx, bodyTop), 28, bodyHeight, Color.Black)

        // draw arms
        val armTop = bodyTop + 6
        val armHeight = min(armTop + 24, dy + CELL_HEIGHT + 1) - armTop
        bmp.fillRect(Pair(9 + dx, armTop), 1, armHeight, Color.Black)
        bmp.fillRect(Pair(24 + dx, armTop), 1, armHeight, Color.Black)
    }

    companion object {
        private const val HORIZONTAL_COUNT = 18
        private const val CELL_WIDTH = 33
        private const val CELL_HEIGHT = 54

        private val eyeColors = mapOf(
            "amb" to Color(0xFFBF00),
            "blu" to Color(0xA1CAF1),
            "brn" to Color(0x9E6B4A),
            "gry" to Color(0x778899),
            "grn" to Color(0x6CA580),
            "hzl" to Color(0xeee8aa),
        )

        private val skinColors = listOf(
            Color(0x8D5524),
            Color(0xC68642),
            Color(0xE0AC69),
            Color(0xF1C27D),
            Color(0xFFDBAC),
        )
    }
}
