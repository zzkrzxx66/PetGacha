package com.example.petgacha.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petgacha.data.repository.PetRepository
import com.example.petgacha.data.repository.PlayerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val petRepository: PetRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {
    
    val coins: StateFlow<Int> = playerRepository.playerData
        .map { it?.coins ?: 1000 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1000)
    
    val collectedPets: StateFlow<Int> = petRepository.collectedPetCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    val totalPets: StateFlow<Int> = petRepository.allPets
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 50)
}