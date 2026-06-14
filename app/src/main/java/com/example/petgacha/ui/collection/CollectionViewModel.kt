package com.example.petgacha.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petgacha.data.model.Pet
import com.example.petgacha.data.model.Rarity
import com.example.petgacha.data.repository.PetRepository
import kotlinx.coroutines.flow.*

class CollectionViewModel(
    private val petRepository: PetRepository
) : ViewModel() {
    
    val allPets: StateFlow<List<Pet>> = petRepository.allPets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val collectedPets: StateFlow<List<Pet>> = petRepository.collectedPets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val collectedCount: StateFlow<Int> = petRepository.collectedPetCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    private val _selectedFilter = MutableStateFlow<Rarity?>(null)
    val selectedFilter: StateFlow<Rarity?> = _selectedFilter.asStateFlow()
    
    fun setFilter(rarity: Rarity?) {
        _selectedFilter.value = rarity
    }
}