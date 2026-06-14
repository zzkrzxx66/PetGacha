package com.example.petgacha.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_data")
data class PlayerData(
    @PrimaryKey
    val id: Int = 1,
    val coins: Int = 1000,
    val pityCounter: Int = 0,
    val totalDraws: Int = 0,
    val lastDailyRewardDate: String = ""
)