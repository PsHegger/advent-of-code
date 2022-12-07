package io.github.pshegger.aoc.y2022

import io.github.pshegger.aoc.common.BaseSolver

class Y2022D7 : BaseSolver() {
    override val year = 2022
    override val day = 7

    private val fileSystem by lazy { parseInput() }
    private val minimumToDelete by lazy { REQUIRED_DISK_SPACE - (DISK_SPACE - fileSystem.size) }

    override fun part1(): Int = fileSystem
        .findDirectories(MAX_SIZE)
        .sumOf { it.size }

    override fun part2(): Int = fileSystem
        .findDirectories(null)
        .filter { it.size > minimumToDelete }
        .minBy { it.size }
        .size

    private fun parseInput() = readInput {
        val lines = readLines()
        val commands = lines.indices
            .filter { lines[it][0] == '$' }
            .map { startIndex ->
                lines.drop(startIndex).take(1) +
                    lines.drop(startIndex + 1).takeWhile { it[0] != '$' }
            }
        val root = Node.Directory("/")
        var current = root
        commands.forEach { commandAndOutput ->
            val command = commandAndOutput[0].drop(2).split(" ").map { it.trim() }
            when (command[0]) {
                "cd" -> {
                    when (command[1]) {
                        "/" -> current = root
                        ".." -> current.parent?.let { current = it }
                        else -> current.content
                            .filterIsInstance<Node.Directory>()
                            .find { it.name == command[1] }
                            ?.let { current = it }
                    }
                }

                "ls" -> {
                    commandAndOutput.drop(1).forEach { outputLine ->
                        val (part1, part2) = outputLine.split(" ", limit = 2)
                        if (part1 == "dir") {
                            current.content.add(Node.Directory(part2, parent = current))
                        } else {
                            current.content.add(Node.File(part2, part1.toInt()))
                        }
                    }
                }
            }
        }
        root
    }

    private sealed class Node(open val name: String) {
        abstract val size: Int

        data class Directory(
            override val name: String,
            val content: MutableList<Node> = mutableListOf(),
            val parent: Directory? = null
        ) : Node(name) {
            override val size: Int
                get() = content.sumOf { it.size }

            fun findDirectories(maxSize: Int?): List<Directory> {
                val self = when {
                    maxSize == null -> listOf(this)
                    size <= maxSize -> listOf(this)
                    else -> emptyList()
                }
                return self + content.filterIsInstance<Directory>().flatMap { it.findDirectories(maxSize) }
            }

            override fun toString(): String = buildString {
                append("Directory(")
                append("name=$name,")
                append("content=$content,")
                append("parent=${parent?.name}")
                append(")")
            }
        }

        data class File(override val name: String, override val size: Int) : Node(name)
    }

    companion object {
        private const val MAX_SIZE = 100000
        private const val DISK_SPACE = 70000000
        private const val REQUIRED_DISK_SPACE = 30000000
    }
}
