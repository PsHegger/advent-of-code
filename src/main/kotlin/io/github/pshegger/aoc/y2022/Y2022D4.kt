package io.github.pshegger.aoc.y2022

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.findCommons

class Y2022D4 : BaseSolver() {
    override val year = 2022
    override val day = 4

    override fun part1(): Int = parseInput()
        .count { (elf1, elf2) ->
            (elf1.first >= elf2.first && elf1.last <= elf2.last) ||
                (elf2.first >= elf1.first && elf2.last <= elf1.last)
        }

    override fun part2(): Int = parseInput()
        .count { (elf1, elf2) ->
            listOf(elf1, elf2).findCommons().isNotEmpty()
        }

    private fun parseInput() = readInput {
        readLines()
            .map { line ->
                val (elf1, elf2) = line.split(",", limit = 2)
                val (elf1Start, elf1End) = elf1.split("-", limit = 2)
                val (elf2Start, elf2End) = elf2.split("-", limit = 2)
                Pair(
                    IntRange(elf1Start.toInt(), elf1End.toInt()),
                    IntRange(elf2Start.toInt(), elf2End.toInt()),
                )
            }
    }
}
