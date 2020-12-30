package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.extendedGCD

class Y2020D13 : BaseSolver() {
    override val year = 2020
    override val day = 13

    override fun part1(): Int {
        val (earliestDeparture, busIds) = parseInput()
        var currentId = earliestDeparture
        while (true) {
            for (id in busIds) {
                if (id == -1) continue
                if (currentId % id == 0) {
                    return (currentId - earliestDeparture) * id
                }
            }
            currentId++
        }
    }

    override fun part2(): Long {
        val data = parseInput().second
        val c = mutableListOf<Int>()
        val m = mutableListOf<Int>()
        var modulus = 1L
        data.indices.forEach { i ->
            if (data[i] != -1) {
                c.add(data[i] - i)
                m.add(data[i])
                modulus *= data[i]
            }
        }
        val ms = m.map { modulus / it }
        val s = m.indices.map { i ->
            extendedGCD(m[i].toLong(), ms[i]).second * ms[i] * c[i]
        }
        return ((s.sum() % modulus) + modulus) % modulus
    }

    private fun parseInput() = readInput {
        val lines = readLines()
        Pair(
            lines[0].toInt(),
            lines[1].split(",").map { it.toIntOrNull() ?: -1 }
        )
    }
}
