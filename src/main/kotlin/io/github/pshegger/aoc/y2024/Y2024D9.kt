package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.model.BaseSolver
import kotlin.math.floor
import kotlin.math.roundToInt

class Y2024D9 : BaseSolver() {
    override val year = 2024
    override val day = 9

    private val input by lazy { parseInput() }

    override fun part1(): Long {
        val disk = input.toBlocks().toMutableList()
        var front = 0
        var back = disk.lastIndex
        while (front < back) {
            when {
                disk[front] != null -> front++
                disk[back] == null -> back--
                else -> {
                    disk[front] = disk[back]
                    disk[back] = null
                    front++
                    back--
                }
            }
        }
        return disk.checksum
    }

    override fun part2(): Long {
        val disk = input.toMutableList()
        val maxFileId = input.maxOf { it.fileId ?: 0 }

        input.filterNot { it.isEmpty }
            .sortedByDescending { it.fileId }
            .forEach { file ->
                // find first empty chunk that is big enough for the file
                val emptyIndex = disk.indexOfFirstEmpty(file.size)
                if (emptyIndex < 0 || disk[emptyIndex].start > file.start) return@forEach
                val emptyChunk = disk[emptyIndex]

                disk.removeAt(emptyIndex)
                disk.add(emptyIndex, file.copy(start = emptyChunk.start))
                if (file.size < emptyChunk.size) {
                    // if file size is smaller than the empty space, we need to add the remaining space as an empty chunk
                    disk.add(
                        index = emptyIndex + 1,
                        element = emptyChunk.copy(
                            start = emptyChunk.start + file.size,
                            size = emptyChunk.size - file.size,
                        )
                    )
                }

                disk.freeUpChunk(file)
            }

        return disk.toBlocks().checksum
    }

    private fun List<Chunk>.toBlocks() = buildList {
        this@toBlocks.forEach { cb ->
            repeat(cb.size) {
                add(cb.fileId)
            }
        }
    }

    private fun List<Chunk>.indexOfFirstEmpty(minSize: Int) = indexOfFirst { it.isEmpty && it.size >= minSize }

    private val List<Int?>.checksum: Long
        get() = mapIndexed { index, fileId ->
            index * (fileId ?: 0).toLong()
        }.sum()

    private fun parseInput() = readInput {
        val input = readLines()[0]

        buildList {
            var fileId = 0
            var isFile = true
            var start = 0
            input.forEach { c ->
                val size = c.digitToInt()
                add(
                    Chunk(
                        fileId = if (isFile) fileId else null,
                        start = start,
                        size = size,
                    )
                )
                start += size
                if (isFile) fileId++
                isFile = !isFile
            }
        }
    }

    private fun MutableList<Chunk>.freeUpChunk(chunk: Chunk) {
        val originalFileChunkIndex = indexOf(chunk)
        var freedUpChunkStart = chunk.start
        var freedUpChunkSize = chunk.size
        var insertIndex = originalFileChunkIndex

        if (originalFileChunkIndex < lastIndex && get(originalFileChunkIndex + 1).isEmpty) {
            // there's an empty chunk after the file, let's merge it with the newly freed up space
            freedUpChunkSize += get(originalFileChunkIndex + 1).size
            removeAt(originalFileChunkIndex + 1)
        }

        if (originalFileChunkIndex > 0 && get(originalFileChunkIndex - 1).isEmpty) {
            // there's an empty chunk before the file, let's merge it with the newly freed up space
            val freeChunk = get(originalFileChunkIndex - 1)
            freedUpChunkSize += freeChunk.size
            freedUpChunkStart = freeChunk.start
            removeAt(originalFileChunkIndex - 1)
            insertIndex--
        }

        remove(chunk)
        add(
            index = insertIndex,
            element = Chunk(
                fileId = null,
                size = freedUpChunkSize,
                start = freedUpChunkStart,
            )
        )
    }

    private data class Chunk(val fileId: Int?, val start: Int, val size: Int) {
        val isEmpty: Boolean = fileId == null

        override fun toString(): String = "$fileId:$start($size)"
    }
}