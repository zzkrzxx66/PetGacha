package com.example.petgacha.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey
    val id: String,
    val name: String,
    val rarity: Rarity,
    val health: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    val skillName: String,
    val skillDescription: String,
    val story: String,
    val imageFileName: String,
    val isCollected: Boolean = false
)

enum class Rarity {
    N, R, SR, SSR, UR
}