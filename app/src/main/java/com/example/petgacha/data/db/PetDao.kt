package com.example.petgacha.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petgacha.data.model.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pets ORDER BY rarity DESC, name ASC")
    fun getAllPets(): Flow<List<Pet>>

    @Query("SELECT * FROM pets WHERE id = :petId")
    fun getPetById(petId: String): Flow<Pet?>

    @Query("SELECT * FROM pets WHERE isCollected = 1 ORDER BY rarity DESC, name ASC")
    fun getCollectedPets(): Flow<List<Pet>>

    @Query("SELECT COUNT(*) FROM pets WHERE isCollected = 1")
    fun getCollectedPetCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPets(pets: List<Pet>)

    @Update
    suspend fun updatePet(pet: Pet)

    @Query("UPDATE pets SET isCollected = 1 WHERE id = :petId")
    suspend fun markPetAsCollected(petId: String)

    @Query("SELECT * FROM pets WHERE rarity = :rarity AND isCollected = 0")
    fun getUncollectedPetsByRarity(rarity: String): Flow<List<Pet>>

    @Query("SELECT * FROM pets WHERE rarity = :rarity")
    fun getPetsByRarity(rarity: String): Flow<List<Pet>>

    @Query("SELECT * FROM pets WHERE rarity = :rarity AND isCollected = 1")
    fun getCollectedPetsByRarity(rarity: String): Flow<List<Pet>>
}