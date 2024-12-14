package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.model.Coordinate
import io.github.pshegger.aoc.common.model.Grid

class Y2024D8 : BaseSolver() {
    override val year = 2024
    override val day = 8

    private val antennaMap by lazy { parseInput() }
    private val antennas by lazy {
        val antennas = mutableMapOf<Char, Set<Coordinate>>()
        antennaMap.entries.forEach { (pos, signal) ->
            signal?.let {
                val coords = antennas.getOrDefault(signal, emptySet()).toMutableSet()
                coords.add(pos)
                antennas[signal] = coords
            }
        }
        antennas.toMap()
    }

    override fun part1(): Int = antennas.keys
        .flatMap { signal ->
            getAntennaPairs(signal).flatMap { (c1, c2) -> getAntinodes(c1, c2, false) }
        }
        .filter { antennaMap.isInside(it) }
        .toSet()
        .size

    override fun part2(): Int = antennas.keys
        .flatMap { signal ->
            getAntennaPairs(signal).flatMap { (c1, c2) -> getAntinodes(c1, c2, true) }
        }
        .toSet()
        .size

    private fun getAntennaPairs(signal: Char) = buildList {
        val antennasForSignal = antennas[signal]?.toList() ?: return@buildList
        antennasForSignal.indices.forEach { i ->
            (i + 1 until antennasForSignal.size).forEach {
                add(Pair(antennasForSignal[i], antennasForSignal[it]))
            }
        }
    }

    private fun getAntinodes(c1: Coordinate, c2: Coordinate, countResonants: Boolean) = buildSet {
        val d = c1 - c2
        if (!countResonants) {
            add(c1 + d)
            add(c2 - d)
            return@buildSet
        }

        var antinode = c1
        while (antennaMap.isInside(antinode)) {
            add(antinode)
            antinode += d
        }

        antinode = c2
        while (antennaMap.isInside(antinode)) {
            add(antinode)
            antinode -= d
        }
    }

    private fun parseInput() = readInput {
        Grid.fromLines(readLines(), { null }) { c ->
            when (c) {
                '.' -> null
                else -> c
            }
        }
    }
}