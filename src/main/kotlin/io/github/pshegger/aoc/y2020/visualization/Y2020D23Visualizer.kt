package io.github.pshegger.aoc.y2020.visualization

import io.github.pshegger.aoc.common.Bitmap
import io.github.pshegger.aoc.common.Color
import io.github.pshegger.aoc.common.TaskVisualizer
import io.github.pshegger.aoc.y2020.Y2020D23
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class Y2020D23Visualizer(private val startingNumbers: List<Int>) : TaskVisualizer {

    override fun generateVisualization() {
        if (!IS_ENABLED) return

        val (cups, minCup, maxCup, indices) = Y2020D23.computeStart(startingNumbers)
        var currentCupIndex = 0
        var frameCtr = 1
        repeat(100) {
            cups.createFrame(frameCtr++)

            val pickUp = listOf(
                cups[currentCupIndex].next,
                cups[currentCupIndex].next.next,
                cups[currentCupIndex].next.next.next,
            )

            val pickUpVals = pickUp.map { it.value }
            cups.createFrame(frameCtr++, pickUpVals)

            var destValue = cups[currentCupIndex].value - 1
            if (destValue < minCup) destValue = maxCup
            while (pickUp.any { it.value == destValue }) {
                destValue--
                if (destValue < minCup) destValue = maxCup
            }
            while (pickUp.first().prev.value != destValue) {
                val n = pickUp.last().next
                n.next.prev = pickUp.last()
                pickUp.last().next = n.next
                n.prev = pickUp.first().prev
                n.prev.next = n
                n.next = pickUp.first()
                pickUp.first().prev = n
                cups.createFrame(frameCtr++, pickUpVals)
            }

            currentCupIndex = indices[cups[currentCupIndex].next.value] ?: error("WTF")
            cups.createFrame(frameCtr++)
        }
        println(cups.arrangeList())
    }

    private fun List<Y2020D23.Node<Int>>.createFrame(ctr: Int, selected: List<Int> = emptyList()) {
        Bitmap(IMAGE_SIZE, IMAGE_SIZE).let { bmp ->
            draw(bmp, selected)
            bmp.save("visualizations/y2020d23/${String.format("%05d", ctr)}.ppm")
        }
    }

    private fun List<Y2020D23.Node<Int>>.draw(bmp: Bitmap, selected: List<Int> = emptyList()) {
        val ls = arrangeList()

        val radPerIndex = 2 * PI / size
        val r = IMAGE_SIZE / 20
        val outerCircleRadius = IMAGE_SIZE / 2 - r - 10
        val innerCircleRadius = outerCircleRadius - 4 * r
        bmp.drawCircle(Pair(IMAGE_SIZE / 2, IMAGE_SIZE / 2), outerCircleRadius, Color.Black)
        bmp.drawCircle(Pair(IMAGE_SIZE / 2, IMAGE_SIZE / 2), innerCircleRadius, Color.Black)

        ls.forEachIndexed { index, n ->
            val rotation = index * radPerIndex - PI / 2
            val positionalRadius = if (n in selected) innerCircleRadius else outerCircleRadius
            val cx = (cos(rotation) *  positionalRadius).roundToInt() + IMAGE_SIZE / 2
            val cy = (sin(rotation) * positionalRadius).roundToInt() + IMAGE_SIZE / 2
            bmp.fillCircle(Pair(cx,cy), r, n.color)
        }
    }

    private fun <T> List<Y2020D23.Node<T>>.arrangeList(): List<T> {
        val ls = mutableListOf<T>()
        var current = first()

        do {
            ls.add(current.value)
            current = current.next
        } while (current != first())

        return ls
    }

    private val Int.color: Color get() = colors[this - 1]

    companion object {
        private const val IS_ENABLED = false

        private const val IMAGE_SIZE = 400

        private val colors = listOf(
            Color(0xFFBF00),
            Color(0xFF8C00),
            Color(0xCD5C5C),
            Color(0xBA55D3),
            Color(0x6A5ACD),
            Color(0x6495ED),
            Color(0x20B2AA),
            Color(0x2E8B57),
            Color(0x9ACD32),
        )
    }
}
