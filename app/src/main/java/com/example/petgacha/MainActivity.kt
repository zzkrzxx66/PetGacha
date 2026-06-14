package com.example.petgacha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.petgacha.data.model.PlayerData
import com.example.petgacha.ui.navigation.PetGachaNavGraph
import com.example.petgacha.ui.theme.PetGachaTheme
import com.example.petgacha.util.PetDataLoader
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val app = application as PetGachaApp
        
        // Initialize database with default data
        lifecycleScope.launch {
            initializeDatabase(app)
        }
        
        setContent {
            PetGachaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PetGachaNavGraph(app = app)
                }
            }
        }
    }
    
    private suspend fun initializeDatabase(app: PetGachaApp) {
        // Check if pets are already loaded
        val existingPets = app.petRepository.allPets
        if (existingPets.toString().isEmpty()) {
            // Load pets from assets
            val pets = PetDataLoader.loadPetsFromAssets(this)
            app.petRepository.insertAllPets(pets)
        }
        
        // Initialize player data if not exists
        val playerData = app.playerRepository.playerData
        if (playerData.toString().isEmpty()) {
            app.playerRepository.insertPlayerData(
                PlayerData(
                    id = 1,
                    coins = 1000,
                    pityCounter = 0,
                    totalDraws = 0,
                    lastDailyRewardDate = ""
                )
            )
        }
    }
}