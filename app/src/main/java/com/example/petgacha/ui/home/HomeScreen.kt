package com.example.petgacha.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petgacha.data.model.Pet

@Composable
fun HomeScreen(
    coins: Int,
    collectedPets: Int,
    totalPets: Int,
    onDrawClick: () -> Unit,
    onCollectionClick: () -> Unit,
    onTeamClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "宠物精灵抽卡",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Coins display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💰 $coins 金币",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        
        // Draw buttons
        Button(
            onClick = onDrawClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(bottom = 8.dp)
        ) {
            Text("单抽 100", style = MaterialTheme.typography.titleMedium)
        }
        
        Button(
            onClick = onDrawClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(bottom = 16.dp)
        ) {
            Text("十连 900", style = MaterialTheme.typography.titleMedium)
        }
        
        // Progress
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "已收集: $collectedPets/$totalPets",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                LinearProgressIndicator(
                    progress = { if (totalPets > 0) collectedPets.toFloat() / totalPets else 0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onCollectionClick) {
                Text("图鉴")
            }
            Button(onClick = onTeamClick) {
                Text("阵容")
            }
        }
    }
}