package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.TaskVisualizer
import io.github.pshegger.aoc.y2020.visualization.Y2020D23Visualizer

class Y2020D23 : BaseSolver() {
    override val year = 2020
    override val day = 23

    override fun getVisualizer(): TaskVisualizer = Y2020D23Visualizer(parseInput())

    override fun part1(): String {
        val inputNumbers = parseInput()
        val index1 = inputNumbers.indexOf(1)
        val cups = simulateGame(inputNumbers, 100)
        return buildString {
            var currentNode = cups[index1].next
            while (currentNode.value != 1) {
                append(currentNode.value)
                currentNode = currentNode.next
            }
        }
    }

    override fun part2(): Long {
        val inputNumbers = parseInput()
        val startingNumbers = inputNumbers + (inputNumbers.maxOf { it } + 1..1_000_000)
        val index1 = inputNumbers.indexOf(1)
        val cups = simulateGame(startingNumbers, 10_000_000)
        val nextVal = cups[index1].next.value
        val next2Val = cups[index1].next.next.value
        return next2Val.toLong() * nextVal
    }

    private fun simulateGame(startingNumbers: List<Int>, roundCount: Int): List<Node<Int>> {
        val (cups, minCup, maxCup, indices) = computeStart(startingNumbers)
        var currentCupIndex = 0
        repeat(roundCount) {
            val pickUp = listOf(
                cups[currentCupIndex].next,
                cups[currentCupIndex].next.next,
                cups[currentCupIndex].next.next.next,
            )
            cups[currentCupIndex].next = pickUp.last().next
            pickUp.last().next.prev = cups[currentCupIndex]
            var destination = cups[currentCupIndex].value - 1
            if (destination < minCup) destination = maxCup
            while (pickUp.any { it.value == destination }) {
                destination--
                if (destination < minCup) destination = maxCup
            }
            val destinationIndex = indices[destination] ?: error("WTF")
            val n0 = cups[destinationIndex]
            val n1 = cups[destinationIndex].next
            n1.prev = pickUp.last()
            pickUp.last().next = n1
            n0.next = pickUp.first()
            pickUp.first().prev = n0
            currentCupIndex = indices[cups[currentCupIndex].next.value] ?: error("WTF")
        }
        return cups
    }

    private fun parseInput() = readInput { readLines().first().trim().map { "$it".toInt() } }

    data class Node<T>(val value: T) {
        lateinit var prev: Node<T>
        lateinit var next: Node<T>
    }

    data class StartData(val cups: List<Node<Int>>, val minCup: Int, val maxCup: Int, val indices: Map<Int, Int>)

    companion object {
        fun computeStart(startingNumbers: List<Int>): StartData {
            val cups = startingNumbers.map { Node(it) }
            val minCup = startingNumbers.minOf { it }
            val maxCup = startingNumbers.maxOf { it }
            val indices = startingNumbers.mapIndexed { index, value -> Pair(value, index) }.toMap()
            cups.forEachIndexed { index, node ->
                if (index == 0) {
                    node.prev = cups.last()
                } else {
                    node.prev = cups[index - 1]
                }
                if (index == cups.lastIndex) {
                    node.next = cups.first()
                } else {
                    node.next = cups[index + 1]
                }
            }
            return StartData(cups, minCup, maxCup, indices)
        }
    }
}
