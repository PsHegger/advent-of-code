package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.model.Coordinate
import io.github.pshegger.aoc.y2020.visualization.Y2020D20Visualizer
import kotlin.math.roundToInt
import kotlin.math.sqrt

@ExperimentalStdlibApi
class Y2020D20 : BaseSolver() {
    override val year = 2020
    override val day = 20

    private val tiles = parseInput()
    private val adjacencies = getAdjacencies()

    private val assembledImage by lazy { arrangeImage() }

    override fun getVisualizer() = Y2020D20Visualizer(assembledImage)

    override fun part1(): Long {
        return adjacencies
            .filter { it.value.size == 2 }
            .map { it.key }
            .fold(1L) { acc, id -> acc * id }
    }

    override fun part2(): Int {
        val seaMonsterTiles = assembledImage.seaMonsterCount() * seaMonster.size

        return assembledImage.fields.sumOf { row -> row.count { it == '#' } } - seaMonsterTiles
    }

    private fun arrangeImage(): Tile {
        val tilesPerSide = sqrt(tiles.size.toFloat()).roundToInt()
        val (topLeftId, topLeftAdjacents) = adjacencies.entries.first { it.value.size == 2 }
        val topLeft = tiles.first { it.id == topLeftId }
        val topLeftRight = tiles.first { it.id == topLeftAdjacents.first() }
        val topLeftBot = tiles.first { it.id == topLeftAdjacents.last() }
        val start = topLeft.permutations.flatMap { tl ->
            topLeftRight.permutations.mapNotNull { tlr ->
                if (tl.canBeLeftOf(tlr)) {
                    Pair(tl, tlr)
                } else {
                    null
                }
            }
        }.first {(tl, _) ->
            topLeftBot.permutations.any { it.canBeBelow(tl) }
        }
        val imageTiles = mutableListOf(mutableListOf(start.first, start.second))
        val placedTiles = mutableSetOf(start.first.id, start.second.id)
        for (x in 2 until tilesPerSide) {
            val possibleNexts = adjacentOf(imageTiles[0][x - 1].id) - placedTiles
            val nextId = possibleNexts.minByOrNull { id -> (adjacentOf(id) - placedTiles).size }
                ?: error("WTF")
            val nextTile = tiles.first { it.id == nextId }.permutationToBeRightOf(imageTiles[0][x - 1])
            imageTiles[0].add(nextTile)
            placedTiles.add(nextTile.id)
        }
        for (y in 1 until tilesPerSide) {
            val firstId = (adjacentOf(imageTiles[y - 1][0].id) - placedTiles).single()
            val row = mutableListOf(
                tiles.first { it.id == firstId }.permutationToBeBelow(imageTiles[y - 1][0])
            )
            placedTiles.add(firstId)
            for (x in 1 until tilesPerSide) {
                val above = imageTiles[y - 1][x]
                val left = row[x - 1]
                val selfId = adjacentOf(above.id).intersect(adjacentOf(left.id) - placedTiles).single()
                row.add(
                    tiles.first { it.id == selfId }.permutations.first { it.canBeBelow(above) && it.canBeRightOf(left) }
                )
                placedTiles.add(selfId)
            }
            imageTiles.add(row)
        }

        val fullImage = imageTiles.merge()
        return fullImage.permutations.maxByOrNull { it.seaMonsterCount() } ?: error("WTF")
    }

    private fun List<List<Tile>>.merge() = Tile(
        -1,
        map { row -> row.map { it.removeEdges() } }.flatMap { row ->
            row[0].indices.map { y -> buildString { row.forEach { append(it[y]) } } }
        }
    )

    private fun List<List<Tile>>.print() {
        for (tileRow in this) {
            for (x in 0..9) {
                println(tileRow.joinToString(" ") { it.fields[x] })
            }
            println()
        }
    }

    private fun adjacentOf(id: Int) = adjacencies[id] ?: error("No adjacent for $id")

    private fun getAdjacencies() = tiles.associate { tile ->
        Pair(
            tile.id,
            tile.permutations
                .flatMap { p ->
                    (tiles - tile).flatMap { it.permutations }.filter { p.canBeAdjacentTo(it) }.map { it.id }
                }.toSet()
        )
    }

    private fun parseInput() = readInput {
        readLines().chunked(12).map { Tile.parseInput(it) }
    }

    data class Tile(val id: Int, val fields: List<String>) {

        val permutations: List<Tile> by lazy {
            val rotations = (1..3).fold(listOf(this)) { acc, _ -> acc + acc.last().rotate() }
            val rotationsFlips = rotations.map { it.flipVertical() }
            rotations + rotationsFlips
        }

        private val topEdge by lazy { fields.first() }
        private val bottomEdge by lazy { fields.last() }
        private val leftEdge by lazy { fields.indices.joinToString("") { fields[it][0].toString() } }
        private val rightEdge by lazy { fields.indices.joinToString("") { fields[it][fields.lastIndex].toString() } }

        fun seaMonsterCoords(): List<Coordinate> = (0..fields.lastIndex - 2).flatMap { y ->
            (0..(fields[0].lastIndex - 19)).mapNotNull { x ->
                val isMonster = seaMonster.all { (dx, dy) -> fields[y + dy][x + dx] == '#' }
                if (isMonster) Coordinate(x, y) else null
            }
        }

        fun seaMonsterCount(): Int = seaMonsterCoords().size

        fun removeEdges(): List<String> = fields
            .drop(1)
            .dropLast(1)
            .map { it.drop(1).dropLast(1) }

        fun permutationToBeRightOf(other: Tile) =
            permutations.first { it.canBeRightOf(other) }

        fun permutationToBeBelow(other: Tile) =
            permutations.first { it.canBeBelow(other) }

        fun canBeBelow(other: Tile) = other.bottomEdge == topEdge
        private fun canBeAbove(other: Tile) = other.topEdge == bottomEdge
        fun canBeLeftOf(other: Tile) = other.leftEdge == rightEdge
        fun canBeRightOf(other: Tile) = other.rightEdge == leftEdge

        fun canBeAdjacentTo(other: Tile) =
            canBeAbove(other) || canBeBelow(other) || canBeLeftOf(other) || canBeRightOf(other)

        private fun rotate() = Tile(
            id,
            fields.indices.map { y ->
                (fields.lastIndex downTo 0).joinToString("") { x ->
                    fields[x][y].toString()
                }
            }
        )

        private fun flipVertical() = Tile(
            id,
            fields.reversed()
        )

        override fun toString(): String = buildString { fields.forEach { append("$it\n") } }

        companion object {
            fun parseInput(lines: List<String>): Tile =
                Tile(
                    lines[0].drop(5).dropLast(1).toInt(),
                    lines.drop(1).take(10)
                )
        }
    }

    companion object {
        val seaMonster = listOf(
            Pair(0, 1),
            Pair(1, 2),
            Pair(4, 2),
            Pair(5, 1),
            Pair(6, 1),
            Pair(7, 2),
            Pair(10, 2),
            Pair(11, 1),
            Pair(12, 1),
            Pair(13, 2),
            Pair(16, 2),
            Pair(17, 1),
            Pair(18, 1),
            Pair(19, 1),
            Pair(18, 0),
        )
    }
}
