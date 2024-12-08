package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2020D21 : BaseSolver() {
    override val year = 2020
    override val day = 21

    override fun part1(): Int {
        val foods = parseInput()
        val possibilities = foods.calculatePossibleAllergens()

        val nonAllergenic = foods.fold(emptySet<String>()) { acc, food -> acc + food.ingredients } -
                possibilities.values.fold(emptySet()) { acc, set -> acc + set }

        return foods.sumOf { food -> food.ingredients.count { it in nonAllergenic } }
    }

    override fun part2(): String {
        val possibilities = parseInput().calculatePossibleAllergens().toMutableMap()
        val solved = mutableMapOf<String, String>()
        while (possibilities.isNotEmpty()) {
            val (key, valueSet) = possibilities.entries.first { it.value.size == 1 }
            val value = valueSet.single()
            possibilities.remove(key)
            for ((otherKey, otherValue) in possibilities) {
                possibilities[otherKey] = otherValue - value
            }
            solved[key] = value
        }

        return solved.toList().sortedBy { it.first }.joinToString(",") { it.second }
    }

    private fun List<Food>.calculatePossibleAllergens(): Map<String, Set<String>> =
        fold(mapOf()) { acc0, food ->
            food.allergens.fold(acc0) { acc, allergen ->
                if (allergen in acc) {
                    (acc - allergen) + mapOf(
                        Pair(
                            allergen,
                            (acc[allergen] ?: error("WTF")).intersect(food.ingredients)
                        )
                    )
                } else {
                    (acc - allergen) + mapOf(allergen to food.ingredients)
                }
            }
        }

    private fun parseInput() = readInput {
        readLines().map { line ->
            val ingredients = line.takeWhile { it != '(' }
                .split(" ")
                .filter { it.isNotBlank() }
                .toSet()
            val allergens = line.dropWhile { it != '(' }
                .drop(1)
                .dropLast(1)
                .split(",")
                .mapIndexed { index, s -> if (index == 0) s.split(" ")[1].trim() else s.trim() }
                .toSet()
            Food(ingredients, allergens)
        }
    }

    private data class Food(val ingredients: Set<String>, val allergens: Set<String>)
}
