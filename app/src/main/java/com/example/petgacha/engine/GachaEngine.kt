package com.example.petgacha.engine

import com.example.petgacha.data.model.DrawResult
import com.example.petgacha.data.model.Pet
import com.example.petgacha.data.model.Rarity
import kotlin.random.Random

class GachaEngine {
    // Probability rates for each rarity
    private val rarityRates = mapOf(
        Rarity.N to 0.50,
        Rarity.R to 0.28,
        Rarity.SR to 0.15,
        Rarity.SSR to 0.06,
        Rarity.UR to 0.01
    )

    // Pity system thresholds
    private val SSR_PITY_THRESHOLD = 50
    private val SR_PITY_THRESHOLD = 10

    /**
     * Perform a single draw
     * @param pets List of all available pets
     * @param pityCounter Current pity counter
     * @param coins Current coins
     * @return DrawResult containing the drawn pet and whether it's new
     */
    fun performSingleDraw(pets: List<Pet>, pityCounter: Int, coins: Int): DrawResult? {
        if (coins < SINGLE_DRAW_COST) return null

        val rarity = determineRarity(pityCounter)
        val availablePets = pets.filter { it.rarity == rarity }
        if (availablePets.isEmpty()) return null

        val selectedPet = availablePets.random()
        return DrawResult(
            pet = selectedPet,
            isNew = !selectedPet.isCollected,
            coinsSpent = SINGLE_DRAW_COST
        )
    }

    /**
     * Perform a ten-draw with guaranteed SR+ in the last slot
     * @param pets List of all available pets
     * @param pityCounter Current pity counter
     * @param coins Current coins
     * @return List of DrawResults
     */
    fun performTenDraw(pets: List<Pet>, pityCounter: Int, coins: Int): List<DrawResult>? {
        if (coins < TEN_DRAW_COST) return null

        val results = mutableListOf<DrawResult>()
        var currentPity = pityCounter

        // First 9 draws
        for (i in 0 until 9) {
            val rarity = determineRarity(currentPity)
            val availablePets = pets.filter { it.rarity == rarity }
            if (availablePets.isEmpty()) continue

            val selectedPet = availablePets.random()
            results.add(DrawResult(
                pet = selectedPet,
                isNew = !selectedPet.isCollected,
                coinsSpent = 0 // Cost will be calculated at the end
            ))

            // Update pity counter
            currentPity = if (rarity >= Rarity.SSR) 0 else currentPity + 1
        }

        // Last draw is guaranteed SR+
        val lastDrawRarity = determineRarityForGuaranteedSR(currentPity)
        val availableLastDrawPets = pets.filter { it.rarity == lastDrawRarity }
        if (availableLastDrawPets.isNotEmpty()) {
            val selectedPet = availableLastDrawPets.random()
            results.add(DrawResult(
                pet = selectedPet,
                isNew = !selectedPet.isCollected,
                coinsSpent = 0
            ))
        }

        // Update total coins spent
        return results.map { it.copy(coinsSpent = TEN_DRAW_COST / 10) }
    }

    /**
     * Determine rarity based on pity counter
     */
    private fun determineRarity(pityCounter: Int): Rarity {
        // If we've reached SSR pity threshold, force SSR or higher
        if (pityCounter >= SSR_PITY_THRESHOLD) {
            return if (Random.nextDouble() < 0.5) Rarity.SSR else Rarity.UR
        }

        // Normal random draw
        val random = Random.nextDouble()
        var cumulative = 0.0

        for ((rarity, rate) in rarityRates) {
            cumulative += rate
            if (random < cumulative) {
                return rarity
            }
        }

        return Rarity.N // Fallback
    }

    /**
     * Determine rarity for guaranteed SR+ draw
     */
    private fun determineRarityForGuaranteedSR(pityCounter: Int): Rarity {
        // If we've reached SSR pity threshold, force SSR or higher
        if (pityCounter >= SSR_PITY_THRESHOLD) {
            return if (Random.nextDouble() < 0.5) Rarity.SSR else Rarity.UR
        }

        // Otherwise, force SR or higher
        val random = Random.nextDouble()
        return when {
            random < 0.06 -> Rarity.SSR
            random < 0.21 -> Rarity.SR // 0.06 + 0.15
            else -> Rarity.SR // Fallback to SR
        }
    }

    companion object {
        const val SINGLE_DRAW_COST = 100
        const val TEN_DRAW_COST = 900
    }
}