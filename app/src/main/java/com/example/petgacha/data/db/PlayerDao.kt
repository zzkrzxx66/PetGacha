package com.example.petgacha.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petgacha.data.model.PlayerData
import com.example.petgacha.data.model.TeamPet
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM player_data WHERE id = 1")
    fun getPlayerData(): Flow<PlayerData?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerData(playerData: PlayerData)

    @Update
    suspend fun updatePlayerData(playerData: PlayerData)

    @Query("UPDATE player_data SET coins = :coins WHERE id = 1")
    suspend fun updateCoins(coins: Int)

    @Query("UPDATE player_data SET pityCounter = :pityCounter WHERE id = 1")
    suspend fun updatePityCounter(pityCounter: Int)

    @Query("UPDATE player_data SET totalDraws = :totalDraws WHERE id = 1")
    suspend fun updateTotalDraws(totalDraws: Int)

    @Query("UPDATE player_data SET lastDailyRewardDate = :date WHERE id = 1")
    suspend fun updateLastDailyRewardDate(date: String)

    @Query("SELECT * FROM team_pets ORDER BY teamSlot ASC")
    fun getTeamPets(): Flow<List<TeamPet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamPet(teamPet: TeamPet)

    @Query("DELETE FROM team_pets WHERE petId = :petId")
    suspend fun removeTeamPet(petId: String)

    @Query("DELETE FROM team_pets")
    suspend fun clearTeam()
}