package com.example.petgacha

import android.app.Application
import com.example.petgacha.data.db.AppDatabase
import com.example.petgacha.data.repository.PetRepository
import com.example.petgacha.data.repository.PlayerRepository

class PetGachaApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val petRepository by lazy { PetRepository(database.petDao()) }
    val playerRepository by lazy { PlayerRepository(database.playerDao()) }
}