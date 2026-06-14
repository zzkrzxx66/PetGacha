package com.example.petgacha.ui.team

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petgacha.data.model.Pet
import com.example.petgacha.data.model.Rarity
import com.example.petgacha.data.model.TeamPet
import com.example.petgacha.ui.collection.getRarityColor

@Composable
fun TeamScreen(
    teamPets: List<Pet>,
    allCollectedPets: List<Pet>,
    totalPower: Int,
    onAddPetToTeam: (Pet) -> Unit,
    onRemovePetFromTeam: (String) -> Unit,
    showPetSelector: Boolean,
    onShowPetSelector: (Boolean) -> Unit
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
                    text = "👥 我的阵容",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "总战力: $totalPower",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "阵容: ${teamPets.size}/3",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Team slots
        Text(
            text = "阵容位置",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        repeat(3) { index ->
            val pet = teamPets.getOrNull(index)
            TeamSlotCard(
                slotNumber = index + 1,
                pet = pet,
                onRemove = { pet?.let { onRemovePetFromTeam(it.id) } },
                onAdd = { onShowPetSelector(true) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add pet button
        if (teamPets.size < 3) {
            Button(
                onClick = { onShowPetSelector(true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("添加宠物")
            }
        }

        // Pet selector dialog
        if (showPetSelector) {
            PetSelectorDialog(
                availablePets = allCollectedPets.filter { pet -> 
                    teamPets.none { it.id == pet.id } 
                },
                onSelect = { pet ->
                    onAddPetToTeam(pet)
                    onShowPetSelector(false)
                },
                onDismiss = { onShowPetSelector(false) }
            )
        }
    }
}

@Composable
fun TeamSlotCard(
    slotNumber: Int,
    pet: Pet?,
    onRemove: () -> Unit,
    onAdd: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (pet != null) 
                getRarityColor(pet.rarity).copy(alpha = 0.1f) 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "位置 $slotNumber",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(60.dp)
                )
                
                if (pet != null) {
                    Column {
                        Text(
                            text = pet.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = pet.rarity.name,
                            color = getRarityColor(pet.rarity),
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = "空位",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (pet != null) {
                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "移除",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                IconButton(onClick = onAdd) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "添加"
                    )
                }
            }
        }
    }
}

@Composable
fun PetSelectorDialog(
    availablePets: List<Pet>,
    onSelect: (Pet) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择宠物") },
        text = {
            if (availablePets.isEmpty()) {
                Text("没有可用的宠物")
            } else {
                LazyColumn {
                    items(availablePets) { pet ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(pet) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = pet.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = pet.rarity.name,
                                    color = getRarityColor(pet.rarity),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "战力: ${pet.health + pet.attack + pet.defense + pet.speed}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (pet != availablePets.last()) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}