package com.example.petgacha.ui.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petgacha.data.model.Pet
import com.example.petgacha.data.model.TeamPet
import com.example.petgacha.data.repository.PetRepository
import com.example.petgacha.data.repository.PlayerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TeamViewModel(
    private val petRepository: PetRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {
    
    val teamPets: StateFlow<List<TeamPet>> = playerRepository.teamPets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val collectedPets: StateFlow<List<Pet>> = petRepository.collectedPets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    private val _teamPetDetails = MutableStateFlow<List<Pet>>(emptyList())
    val teamPetDetails: StateFlow<List<Pet>> = _teamPetDetails.asStateFlow()
    
    val totalPower: StateFlow<Int> = _teamPetDetails
        .map { pets -> pets.sumOf { it.health + it.attack + it.defense + it.speed } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    private val _showPetSelector = MutableStateFlow(false)
    val showPetSelector: StateFlow<Boolean> = _showPetSelector.asStateFlow()
    
    init {
        viewModelScope.launch {
            teamPets.collect { teamList ->
                val petDetails = teamList.mapNotNull { teamPet ->
                    petRepository.getPetById(teamPet.petId).first()
                }
                _teamPetDetails.value = petDetails
            }
        }
    }
    
    fun addPetToTeam(pet: Pet) {
        viewModelScope.launch {
            val currentTeam = teamPets.value
            if (currentTeam.size < 3) {
                val nextSlot = (1..3).firstOrNull { slot -> 
                    currentTeam.none { it.teamSlot == slot } 
                } ?: return@launch
                playerRepository.insertTeamPet(TeamPet(pet.id, nextSlot))
            }
        }
    }
    
    fun removePetFromTeam(petId: String) {
        viewModelScope.launch {
            playerRepository.removeTeamPet(petId)
        }
    }
    
    fun showPetSelector(show: Boolean) {
        _showPetSelector.value = show
    }
}