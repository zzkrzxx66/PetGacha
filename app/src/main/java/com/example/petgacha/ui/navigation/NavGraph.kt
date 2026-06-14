package com.example.petgacha.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.petgacha.PetGachaApp
import com.example.petgacha.data.model.Pet
import com.example.petgacha.data.model.Rarity
import com.example.petgacha.ui.collection.CollectionScreen
import com.example.petgacha.ui.collection.CollectionViewModel
import com.example.petgacha.ui.detail.PetDetailDialog
import com.example.petgacha.ui.gacha.GachaScreen
import com.example.petgacha.ui.gacha.GachaViewModel
import com.example.petgacha.ui.home.HomeScreen
import com.example.petgacha.ui.home.HomeViewModel
import com.example.petgacha.ui.team.TeamScreen
import com.example.petgacha.ui.team.TeamViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "主页", Icons.Default.Home)
    object Gacha : Screen("gacha", "抽卡", Icons.Default.Star)
    object Collection : Screen("collection", "图鉴", Icons.Default.List)
    object Team : Screen("team", "阵容", Icons.Default.Star)
}

@Composable
fun PetGachaNavGraph(
    app: PetGachaApp,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(app.petRepository, app.playerRepository)
    )
    val gachaViewModel: GachaViewModel = viewModel(
        factory = GachaViewModelFactory(app.petRepository, app.playerRepository)
    )
    val collectionViewModel: CollectionViewModel = viewModel(
        factory = CollectionViewModelFactory(app.petRepository)
    )
    val teamViewModel: TeamViewModel = viewModel(
        factory = TeamViewModelFactory(app.petRepository, app.playerRepository)
    )
    
    var selectedPet by remember { mutableStateOf<Pet?>(null) }
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val screens = listOf(Screen.Home, Screen.Gacha, Screen.Collection, Screen.Team)
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                val coins by homeViewModel.coins.collectAsState()
                val collectedPets by homeViewModel.collectedPets.collectAsState()
                val totalPets by homeViewModel.totalPets.collectAsState()
                
                HomeScreen(
                    coins = coins,
                    collectedPets = collectedPets,
                    totalPets = totalPets,
                    onDrawClick = { navController.navigate(Screen.Gacha.route) },
                    onCollectionClick = { navController.navigate(Screen.Collection.route) },
                    onTeamClick = { navController.navigate(Screen.Team.route) }
                )
            }
            
            composable(Screen.Gacha.route) {
                val coins by gachaViewModel.coins.collectAsState()
                val pityCounter by gachaViewModel.pityCounter.collectAsState()
                val drawResults by gachaViewModel.drawResults.collectAsState()
                
                GachaScreen(
                    coins = coins,
                    pityCounter = pityCounter,
                    onSingleDraw = { gachaViewModel.performSingleDraw() },
                    onTenDraw = { gachaViewModel.performTenDraw() },
                    drawResults = drawResults,
                    onDismissResults = { gachaViewModel.dismissResults() }
                )
            }
            
            composable(Screen.Collection.route) {
                val allPets by collectionViewModel.allPets.collectAsState()
                val collectedCount by collectionViewModel.collectedCount.collectAsState()
                val selectedFilter by collectionViewModel.selectedFilter.collectAsState()
                
                CollectionScreen(
                    pets = allPets,
                    collectedCount = collectedCount,
                    totalCount = allPets.size,
                    onPetClick = { pet -> selectedPet = pet },
                    selectedFilter = selectedFilter,
                    onFilterChanged = { collectionViewModel.setFilter(it) }
                )
            }
            
            composable(Screen.Team.route) {
                val teamPetDetails by teamViewModel.teamPetDetails.collectAsState()
                val collectedPets by teamViewModel.collectedPets.collectAsState()
                val totalPower by teamViewModel.totalPower.collectAsState()
                val showPetSelector by teamViewModel.showPetSelector.collectAsState()
                
                TeamScreen(
                    teamPets = teamPetDetails,
                    allCollectedPets = collectedPets,
                    totalPower = totalPower,
                    onAddPetToTeam = { teamViewModel.addPetToTeam(it) },
                    onRemovePetFromTeam = { teamViewModel.removePetFromTeam(it) },
                    showPetSelector = showPetSelector,
                    onShowPetSelector = { teamViewModel.showPetSelector(it) }
                )
            }
        }
    }
    
    // Pet detail dialog
    selectedPet?.let { pet ->
        PetDetailDialog(
            pet = pet,
            onDismiss = { selectedPet = null }
        )
    }
}

class HomeViewModelFactory(
    private val petRepository: com.example.petgacha.data.repository.PetRepository,
    private val playerRepository: com.example.petgacha.data.repository.PlayerRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(petRepository, playerRepository) as T
    }
}

class GachaViewModelFactory(
    private val petRepository: com.example.petgacha.data.repository.PetRepository,
    private val playerRepository: com.example.petgacha.data.repository.PlayerRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return GachaViewModel(petRepository, playerRepository) as T
    }
}

class CollectionViewModelFactory(
    private val petRepository: com.example.petgacha.data.repository.PetRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return CollectionViewModel(petRepository) as T
    }
}

class TeamViewModelFactory(
    private val petRepository: com.example.petgacha.data.repository.PetRepository,
    private val playerRepository: com.example.petgacha.data.repository.PlayerRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return TeamViewModel(petRepository, playerRepository) as T
    }
}