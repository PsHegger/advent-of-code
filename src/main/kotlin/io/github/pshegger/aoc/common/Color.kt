package io.github.pshegger.aoc.common

import kotlin.random.Random

data class Color(val red: Double, val green: Double, val blue: Double) {
    constructor(red: Int, green: Int, blue: Int) : this(
        red.toDouble() / 255,
        green.toDouble() / 255,
        blue.toDouble() / 255
    )
    constructor(rgb: Int) : this(
        rgb shr 16 and 0xFF,
        rgb shr 8 and 0xFF,
        rgb and 0xFF
    )

    fun toPPMPixel() = buildString {
        val r = (255.999 * red).toInt()
        val g = (255.999 * green).toInt()
        val b = (255.999 * blue).toInt()

        append("$r $g $b")
    }

    companion object {
        val Black = Color(0x000000)
        val White = Color(0xFFFFFF)

        fun parseString(str: String): Color? {
            val hexes = rgbHexRegex.matchEntire(str)?.groupValues ?: return null
            val r = hexes[1].toInt(16)
            val g = hexes[2].toInt(16)
            val b = hexes[3].toInt(16)
            return Color(r, g, b)
        }

        fun random() = Random.Default.let { rng ->
            Color(rng.nextDouble(), rng.nextDouble(), rng.nextDouble())
        }

        private val rgbHexRegex = "^#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})$".toRegex()
    }
}
