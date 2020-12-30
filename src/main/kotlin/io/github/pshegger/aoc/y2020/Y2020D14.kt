package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.BaseSolver

class Y2020D14 : BaseSolver() {
    override val year = 2020
    override val day = 14

    override fun part1(): Long = solve(true)
    override fun part2(): Long = solve(false)

    private fun solve(isPart1: Boolean): Long {
        val memory = mutableMapOf<Long, Long>()
        var mask: String? = null
        for (line in parseInput()) {
            if (line.startsWith("mask = ")) {
                mask = line.drop(7)
                continue
            }
            val (addresses, value) = line.toMemorySets(mask, isPart1)
            addresses.forEach { memory[it] = value }
        }

        return memory.values.sum()
    }

    private fun String.toMemorySets(mask: String?, isPart1: Boolean): Pair<List<Long>, Long> {
        val result = setMemoryRegex.matchEntire(this)?.groupValues
            ?: error("Couldn't parse line as set memory instruction")
        val address = result[1].toInt()
        val value = result[2].toInt()

        return if (isPart1) {
            val newValue = value.toBinaryValue().mapIndexed { index, c ->
                if (mask == null || mask[index] == 'X') {
                    c
                } else {
                    mask[index]
                }
            }.joinToString("").toLong(2)
            Pair(listOf(address.toLong()), newValue)
        } else {
            val maskedAddress = address.toBinaryValue().mapIndexed { index, c ->
                when {
                    mask == null || mask[index] == '0' -> c
                    mask[index] == '1' -> '1'
                    else -> 'X'
                }
            }.joinToString("")
            val addresses = calculateFloating(maskedAddress).map { it.toLong(2) }
            Pair(addresses, value.toLong())
        }
    }

    private fun calculateFloating(masked: String): List<String> {
        for (i in masked.indices) {
            if (masked[i] == 'X') {
                val prev = masked.take(i)
                val next = masked.drop(i + 1)
                return calculateFloating("${prev}0$next") + calculateFloating("${prev}1$next")
            }
        }

        return listOf(masked)
    }

    private fun parseInput() = readInput { readLines() }

    private fun Int.toBinaryValue() = String.format("%36s", toString(2)).replace(" ", "0")

    companion object {
        private val setMemoryRegex = "mem\\[(\\d+)] = (\\d+)".toRegex()
    }
}
