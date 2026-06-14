package com.example.petgacha.ui.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.petgacha.data.model.Pet
import com.example.petgacha.data.model.Rarity

@Composable
fun CollectionScreen(
    pets: List<Pet>,
    collectedCount: Int,
    totalCount: Int,
    onPetClick: (Pet) -> Unit,
    selectedFilter: Rarity?,
    onFilterChanged: (Rarity?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📖 宠物图鉴",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "已收集: $collectedCount / $totalCount",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                LinearProgressIndicator(
                    progress = { if (totalCount > 0) collectedCount.toFloat() / totalCount else 0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }

        // Filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedFilter == null,
                onClick = { onFilterChanged(null) },
                label = { Text("全部") }
            )
            Rarity.values().forEach { rarity ->
                FilterChip(
                    selected = selectedFilter == rarity,
                    onClick = { onFilterChanged(rarity) },
                    label = { Text(rarity.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getRarityColor(rarity).copy(alpha = 0.3f)
                    )
                )
            }
        }

        // Pet grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pets.filter { selectedFilter == null || it.rarity == selectedFilter }) { pet ->
                PetCard(pet = pet, onClick = { onPetClick(pet) })
            }
        }
    }
}

@Composable
fun PetCard(pet: Pet, onClick: () -> Unit) {
    val rarityColor = getRarityColor(pet.rarity)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (pet.isCollected) 
                rarityColor.copy(alpha = 0.1f) 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (pet.isCollected) {
            CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(rarityColor, rarityColor.copy(alpha = 0.5f)))
            )
        } else null
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (pet.isCollected) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = pet.name,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = pet.rarity.name,
                        color = rarityColor,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            } else {
                Text(
                    text = "???",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun getRarityColor(rarity: Rarity): Color {
    return when (rarity) {
        Rarity.N -> Color(0xFF9E9E9E)
        Rarity.R -> Color(0xFF4CAF50)
        Rarity.SR -> Color(0xFF2196F3)
        Rarity.SSR -> Color(0xFF9C27B0)
        Rarity.UR -> Color(0xFFFFD700)
    }
}