package com.example.petgacha.ui.gacha

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petgacha.data.model.DrawResult
import com.example.petgacha.data.model.Pet
import com.example.petgacha.data.model.PlayerData
import com.example.petgacha.data.repository.PetRepository
import com.example.petgacha.data.repository.PlayerRepository
import com.example.petgacha.engine.GachaEngine
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GachaViewModel(
    private val petRepository: PetRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {
    
    private val gachaEngine = GachaEngine()
    
    val coins: StateFlow<Int> = playerRepository.playerData
        .map { it?.coins ?: 1000 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1000)
    
    val pityCounter: StateFlow<Int> = playerRepository.playerData
        .map { it?.pityCounter ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    private val _drawResults = MutableStateFlow<List<DrawResult>?>(null)
    val drawResults: StateFlow<List<DrawResult>?> = _drawResults.asStateFlow()
    
    private val allPets = petRepository.allPets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun performSingleDraw() {
        viewModelScope.launch {
            val playerData = playerRepository.playerData.first()
            val pets = allPets.value
            
            if (playerData != null && pets.isNotEmpty()) {
                val result = gachaEngine.performSingleDraw(pets, playerData.pityCounter, playerData.coins)
                if (result != null) {
                    // Update coins
                    playerRepository.updateCoins(playerData.coins - result.coinsSpent)
                    
                    // Update pity counter
                    val newPity = if (result.pet.rarity.ordinal >= 3) 0 else playerData.pityCounter + 1
                    playerRepository.updatePityCounter(newPity)
                    
                    // Mark pet as collected
                    petRepository.markPetAsCollected(result.pet.id)
                    
                    // Update total draws
                    playerRepository.updateTotalDraws(playerData.totalDraws + 1)
                    
                    // Show result
                    _drawResults.value = listOf(result)
                }
            }
        }
    }
    
    fun performTenDraw() {
        viewModelScope.launch {
            val playerData = playerRepository.playerData.first()
            val pets = allPets.value
            
            if (playerData != null && pets.isNotEmpty()) {
                val results = gachaEngine.performTenDraw(pets, playerData.pityCounter, playerData.coins)
                if (results != null) {
                    // Update coins
                    playerRepository.updateCoins(playerData.coins - GachaEngine.TEN_DRAW_COST)
                    
                    // Update pity counter based on best result
                    val bestRarity = results.maxByOrNull { it.pet.rarity.ordinal }?.pet?.rarity
                    val newPity = if (bestRarity != null && bestRarity.ordinal >= 3) 0 
                                 else playerData.pityCounter + results.size
                    playerRepository.updatePityCounter(newPity)
                    
                    // Mark all pets as collected
                    results.forEach { result ->
                        petRepository.markPetAsCollected(result.pet.id)
                    }
                    
                    // Update total draws
                    playerRepository.updateTotalDraws(playerData.totalDraws + results.size)
                    
                    // Show results
                    _drawResults.value = results
                }
            }
        }
    }
    
    fun dismissResults() {
        _drawResults.value = null
    }
}