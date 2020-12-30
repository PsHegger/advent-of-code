package io.github.pshegger.aoc.common

sealed class Answer {
    abstract val part1: String
    abstract val part2: String

    object Empty : Answer() {
        override val part1 = "<unknown>"
        override val part2 = "<unknown>"
    }

    class Part1(p1: Any) : Answer() {
        override val part1 = p1.toString()
        override val part2 = "<unknown>"
    }

    class Full(p1: Any, p2: Any) : Answer() {
        override val part1 = p1.toString()
        override val part2 = p2.toString()
    }

    override fun toString() = "Part 1: $part1\nPart 2: $part2"
}
