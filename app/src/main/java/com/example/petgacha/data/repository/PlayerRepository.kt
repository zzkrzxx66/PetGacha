package com.example.petgacha.data.repository

import com.example.petgacha.data.db.PlayerDao
import com.example.petgacha.data.model.PlayerData
import com.example.petgacha.data.model.TeamPet
import kotlinx.coroutines.flow.Flow

class PlayerRepository(private val playerDao: PlayerDao) {
    val playerData: Flow<PlayerData?> = playerDao.getPlayerData()
    val teamPets: Flow<List<TeamPet>> = playerDao.getTeamPets()

    suspend fun insertPlayerData(playerData: PlayerData) = playerDao.insertPlayerData(playerData)

    suspend fun updatePlayerData(playerData: PlayerData) = playerDao.updatePlayerData(playerData)

    suspend fun updateCoins(coins: Int) = playerDao.updateCoins(coins)

    suspend fun updatePityCounter(pityCounter: Int) = playerDao.updatePityCounter(pityCounter)

    suspend fun updateTotalDraws(totalDraws: Int) = playerDao.updateTotalDraws(totalDraws)

    suspend fun updateLastDailyRewardDate(date: String) = playerDao.updateLastDailyRewardDate(date)

    suspend fun insertTeamPet(teamPet: TeamPet) = playerDao.insertTeamPet(teamPet)

    suspend fun removeTeamPet(petId: String) = playerDao.removeTeamPet(petId)

    suspend fun clearTeam() = playerDao.clearTeam()
}