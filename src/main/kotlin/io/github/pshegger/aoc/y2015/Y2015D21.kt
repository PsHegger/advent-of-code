package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.utils.getCartesianProduct

class Y2015D21 : BaseSolver() {
    override val year = 2015
    override val day = 21

    override fun part1(): Int = generateBuilds()
        .filter { canBeatBoss(it) }
        .minOf { it.cost }

    override fun part2(): Int = generateBuilds()
        .filterNot { canBeatBoss(it) }
        .maxOf { it.cost }

    private fun generateBuilds(): List<Build> =
        listOf(
            weapons.indices,
            listOf(-1) + armors.indices,
            listOf(-1) + rings.indices,
            listOf(-1) + rings.indices,
        )
            .getCartesianProduct()
            .filterNot { it[2] == it[3] && it[2] >= 0 }
            .map { (weaponIndex, armorIndex, ring1Index, ring2Index) ->
                Build(
                    weapon = weapons[weaponIndex],
                    armor = if (armorIndex >= 0) armors[armorIndex] else null,
                    rings = listOfNotNull(
                        if (ring1Index >= 0) rings[ring1Index] else null,
                        if (ring2Index >= 0) rings[ring2Index] else null,
                    )
                )
            }

    private fun canBeatBoss(build: Build): Boolean {
        var playerHP = 100
        var bossHP = BOSS_HP
        var playerTurn = true

        while (playerHP > 0) {
            if (playerTurn) {
                bossHP -= (build.damage - BOSS_ARMOR).coerceAtLeast(1)
            } else {
                playerHP -= (BOSS_DAMAGE - build.defense).coerceAtLeast(1)
            }
            if (bossHP <= 0) {
                return true
            }
            playerTurn = !playerTurn
        }

        return false
    }

    private data class Item(val name: String, val cost: Int, val damage: Int, val armor: Int)

    private data class Build(val weapon: Item, val armor: Item?, val rings: List<Item>) {
        private val allItems = buildList {
            add(weapon)
            armor?.let { add(it) }
            addAll(rings)
        }

        val cost: Int get() = allItems.sumOf { it.cost }
        val damage: Int get() = allItems.sumOf { it.damage }
        val defense: Int get() = allItems.sumOf { it.armor }
    }

    companion object {
        private const val BOSS_HP = 100
        private const val BOSS_DAMAGE = 8
        private const val BOSS_ARMOR = 2

        private val weapons = listOf(
            Item("Dagger", 8, 4, 0),
            Item("Shortsword", 10, 5, 0),
            Item("Warhammer", 25, 6, 0),
            Item("Longsword", 40, 7, 0),
            Item("Greataxe", 74, 8, 0),
        )

        private val armors = listOf(
            Item("Leather", 13, 0, 1),
            Item("Chainmail", 31, 0, 2),
            Item("Splintmail", 53, 0, 3),
            Item("Bandedmail", 75, 0, 4),
            Item("Platemail", 102, 0, 5),
        )

        private val rings = listOf(
            Item("Damage +1", 25, 1, 0),
            Item("Damage +2", 50, 2, 0),
            Item("Damage +3", 100, 3, 0),
            Item("Defense +1", 20, 0, 1),
            Item("Defense +2", 40, 0, 2),
            Item("Defense +3", 80, 0, 3),
        )
    }
}