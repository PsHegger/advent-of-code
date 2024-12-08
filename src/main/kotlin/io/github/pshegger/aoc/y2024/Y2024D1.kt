package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.model.BaseSolver
import kotlin.math.absoluteValue

class Y2024D1 : BaseSolver() {
    override val year = 2024
    override val day = 1

    override fun part1(): Int = parseInput().let { (list1, list2) ->
        list1.sorted()
            .zip(list2.sorted())
            .sumOf { (a, b) ->
                (a - b).absoluteValue
            }
    }

    override fun part2(): Int = parseInput().let { (list1, list2) ->
        val counts = mutableMapOf<Int, Int>()
        list2.forEach {
            val count = counts.getOrDefault(it, 0)
            counts[it] = count + 1
        }
        list1.fold(0) { acc, num ->
            acc + num * counts.getOrDefault(num, 0)
        }
    }

    private fun parseInput() = readInput {
        val list1 = mutableListOf<Int>()
        val list2 = mutableListOf<Int>()

        readLines().forEach { line ->
            val (n1, n2) = line.split(" ")
                .filterNot { it.isEmpty() }
                .map { it.toInt() }
            list1.add(n1)
            list2.add(n2)
        }

        Pair(list1.toList(), list2.toList())
    }
}
