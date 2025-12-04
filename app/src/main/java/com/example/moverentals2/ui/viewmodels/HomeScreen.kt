package com.example.moverentals2.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.moverentals2.R
import com.example.moverentals2.ui.viewmodels.Car
import com.example.moverentals2.ui.viewmodels.FavoritesViewModel
import com.example.moverentals2.ui.viewmodels.HomeViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel = viewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val favoritesUiState by favoritesViewModel.uiState.collectAsState()
    val context = LocalContext.current


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val savedStateHandle = navBackStackEntry?.savedStateHandle


    LaunchedEffect(Unit) {

        homeViewModel.loadCars()
    }


    LaunchedEffect(savedStateHandle) {
        if (savedStateHandle?.contains("price_min") == true) {
            val priceMin = savedStateHandle.get<Float>("price_min")
            val priceMax = savedStateHandle.get<Float>("price_max")
            val ratingMin = savedStateHandle.get<Float>("rating_min")

            homeViewModel.loadCars(priceMin, priceMax, ratingMin)

            savedStateHandle.remove<Float>("price_min")
            savedStateHandle.remove<Float>("price_max")
            savedStateHandle.remove<Float>("rating_min")
        }
    }


    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            homeViewModel.dismissError()
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA)
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "MoveRentals",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { navController.navigate("settings") }) {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = "Ajustes",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = { navController.navigate("filter") },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AAB2))
                    ) {
                        Icon(painterResource(id = R.drawable.ic_filter), contentDescription = "Filter")
                        Spacer(Modifier.width(8.dp))
                        Text("Filter")
                    }
                }
                if (uiState.isLoading) {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    items(uiState.cars) { car ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarCard(
    car: Car,
    navController: NavController,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Box(modifier = Modifier.clickable {
        if (car.id.isNotEmpty()) {
            navController.navigate("car_detail/${car.id}")
        }
    }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (car.isFeatured) Color(0xFF00AAB2) else Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {

                Column(Modifier.weight(1f)) {
                    AsyncImage(
                        model = car.imageUrl,
                        contentDescription = car.name,
                        modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.placeholder_upload_car)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("City: ${car.city}", fontSize = 12.sp, color = if (car.isFeatured) Color.White else Color.Gray)
                    Text("Owner: ${car.owner}", fontSize = 12.sp, color = if (car.isFeatured) Color.White else Color.Gray)
                }
                Spacer(Modifier.width(16.dp))


                Column(modifier = Modifier.weight(0.7f), horizontalAlignment = Alignment.End) {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else (if (car.isFeatured) Color.White else Color.Black)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(car.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (car.isFeatured) Color.White else Color.Black)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Star, contentDescription = "Rating", modifier = Modifier.size(16.dp), tint = if (car.isFeatured) Color.White else Color.Gray)
                        Spacer(Modifier.width(4.dp))

                        Text(String.format(Locale.US, "%.1f", car.ratingAsDouble), fontSize = 14.sp, color = if (car.isFeatured) Color.White else Color.Gray)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("Price per day", fontSize = 12.sp, color = if (car.isFeatured) Color.White else Color.Gray)

                    Text("${car.priceAsString}mx", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = if (car.isFeatured) Color.White else Color.Black)
                }
            }
        }
    }
}
