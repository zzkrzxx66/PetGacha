package com.example.petgacha.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_pets")
data class TeamPet(
    @PrimaryKey
    val petId: String,
    val teamSlot: Int
)