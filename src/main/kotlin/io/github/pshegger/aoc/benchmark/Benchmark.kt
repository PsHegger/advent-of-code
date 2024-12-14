package io.github.pshegger.aoc.benchmark

import kotlin.math.max
import kotlin.time.Duration

sealed class Benchmark {

    abstract val name: String
    abstract val cases: List<Case>

    fun run() {
        val results = cases.map {
            Pair(it.name, it.run().toString())
        }
        val c1Width = max(name.length, results.maxOf { it.first.length })
        val c2Width = max("Duration".length, results.maxOf { it.second.length })
        val divider = buildString {
            append("+-")
            repeat(c1Width) { append('-') }
            append("-+-")
            repeat(c2Width) { append('-') }
            append("-+")
        }
        val out = buildString {
            append("$divider\n")
            append("| ")
            appendWithWidth(name, c1Width)
            append(" | ")
            appendWithWidth("Duration", c2Width)
            append(" |\n")
            append("$divider\n")
            results.forEach { (name, duration) ->
                append("| ")
                appendWithWidth(name, c1Width)
                append(" | ")
                appendWithWidth(duration, c2Width)
                append(" |\n")
            }
            append(divider)
        }
        println(out)
    }

    private fun StringBuilder.appendWithWidth(text: String, width: Int) {
        append(text)
        repeat(width - text.length) { append(' ') }
    }

    data class Case(val name: String, val run: () -> Duration)
}