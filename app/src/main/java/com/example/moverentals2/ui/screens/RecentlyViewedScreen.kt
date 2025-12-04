package com.example.moverentals2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moverentals2.R
import com.example.moverentals2.ui.viewmodels.FavoritesViewModel
import com.example.moverentals2.ui.viewmodels.RecentsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentlyViewedScreen(
    navController: NavController,
    recentsViewModel: RecentsViewModel = viewModel(),
    // También necesitamos el ViewModel de favoritos para el CarCard
    favoritesViewModel: FavoritesViewModel = viewModel()
) {
    val recentsUiState by recentsViewModel.uiState.collectAsState()
    val favoritesUiState by favoritesViewModel.uiState.collectAsState()

    // Cargar los coches vistos recientemente cuando la pantalla aparece
    LaunchedEffect(Unit) {
        recentsViewModel.loadRecentCars()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recently Viewed", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painterResource(id = R.drawable.ic_arrow_back), "Go Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            recentsUiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            recentsUiState.recentCars.isEmpty() && !recentsUiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text("You haven't viewed any cars yet.")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(recentsUiState.recentCars) { car ->
                        // Reutilizamos el mismo CarCard, pasándole la información de favoritos
                        CarCard(
                            car = car,
                            navController = navController,
                            isFavorite = favoritesUiState.favoriteCarIds.contains(car.id),
                            onFavoriteClick = { favoritesViewModel.toggleFavorite(car.id) }
                        )
                    }
                }
            }
        }
    }
}
