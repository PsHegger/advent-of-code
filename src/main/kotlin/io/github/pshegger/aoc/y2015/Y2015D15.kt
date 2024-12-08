package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.extensions.extractAll
import io.github.pshegger.aoc.common.utils.generateSplits
import io.github.pshegger.aoc.common.extensions.toExtractor
import kotlin.math.max

class Y2015D15 : BaseSolver() {
    override val year = 2015
    override val day = 15

    private val extractor = "%s: capacity %d, durability %d, flavor %d, texture %d, calories %d".toExtractor()

    private val ingredients by lazy { parseInput() }
    private val ratios by lazy { generateSplits(ingredients.size, INGREDIENT_COUNT) }
    private val rationedProperties by lazy { ratios.map { ingredients.calculateProperties(it) } }

    override fun part1(): Int = rationedProperties.maxOf { it.score }

    override fun part2(): Int = rationedProperties
        .filter { it.calories == TARGET_CALORIES }
        .maxOf { it.score }

    private fun List<Ingredient>.calculateProperties(ratio: List<Int>): Properties = Properties(
        capacity = max(
            ratio.mapIndexed { i, r -> r * get(i).properties.capacity }.sum(),
            0
        ),
        durability = max(
            ratio.mapIndexed { i, r -> r * get(i).properties.durability }.sum(),
            0
        ),
        flavor = max(
            ratio.mapIndexed { i, r -> r * get(i).properties.flavor }.sum(),
            0
        ),
        texture = max(
            ratio.mapIndexed { i, r -> r * get(i).properties.texture }.sum(),
            0
        ),
        calories = max(
            ratio.mapIndexed { i, r -> r * get(i).properties.calories }.sum(),
            0
        ),
    )

    private fun parseInput() = readInput {
        readLines().extractAll(extractor) {
            Ingredient(
                it[0],
                Properties(
                    it[1].toInt(),
                    it[2].toInt(),
                    it[3].toInt(),
                    it[4].toInt(),
                    it[5].toInt(),
                )
            )
        }
    }

    private data class Ingredient(
        val name: String,
        val properties: Properties
    )

    private data class Properties(
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int
    ) {

        val score: Int get() = capacity * durability * flavor * texture
    }

    companion object {
        private const val INGREDIENT_COUNT = 100
        private const val TARGET_CALORIES = 500
    }
}
