package io.github.pshegger.aoc.common.model

import io.github.pshegger.aoc.common.utils.downloadInputIfMissing
import java.io.File

abstract class BaseSolver {
    abstract val year: Int
    abstract val day: Int

    protected open fun part1(): Any? = null
    protected open fun part2(): Any? = null
    protected open fun getVisualizer(): TaskVisualizer? = null

    protected fun <T> readInput(block: File.() -> T): T {
        downloadInputIfMissing(year, day)
        return with(File("inputs/${year}_$day.txt"), block)
    }

    fun solve(): Answer {
        val p1 = part1() ?: return Answer.Empty
        val p2 = part2() ?: return Answer.Part1(p1)
        getVisualizer()?.generateVisualization()
        return Answer.Full(p1, p2)
    }
}
