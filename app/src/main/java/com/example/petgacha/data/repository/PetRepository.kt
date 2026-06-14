package com.example.petgacha.data.repository

import com.example.petgacha.data.db.PetDao
import com.example.petgacha.data.model.Pet
import kotlinx.coroutines.flow.Flow

class PetRepository(private val petDao: PetDao) {
    val allPets: Flow<List<Pet>> = petDao.getAllPets()
    val collectedPets: Flow<List<Pet>> = petDao.getCollectedPets()
    val collectedPetCount: Flow<Int> = petDao.getCollectedPetCount()

    fun getPetById(petId: String): Flow<Pet?> = petDao.getPetById(petId)

    fun getCollectedPetsByRarity(rarity: String): Flow<List<Pet>> = petDao.getCollectedPetsByRarity(rarity)

    fun getUncollectedPetsByRarity(rarity: String): Flow<List<Pet>> = petDao.getUncollectedPetsByRarity(rarity)

    fun getPetsByRarity(rarity: String): Flow<List<Pet>> = petDao.getPetsByRarity(rarity)

    suspend fun insertPet(pet: Pet) = petDao.insertPet(pet)

    suspend fun insertAllPets(pets: List<Pet>) = petDao.insertAllPets(pets)

    suspend fun updatePet(pet: Pet) = petDao.updatePet(pet)

    suspend fun markPetAsCollected(petId: String) = petDao.markPetAsCollected(petId)
}