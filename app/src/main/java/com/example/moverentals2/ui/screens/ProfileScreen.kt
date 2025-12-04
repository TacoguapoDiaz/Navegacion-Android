package com.example.moverentals2.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moverentals2.R
import com.example.moverentals2.ui.viewmodels.ProfileViewModel
import com.example.moverentals2.ui.viewmodels.Rental
import com.example.moverentals2.ui.viewmodels.User
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    navController: NavController,
    onNavigateToAuth: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val uiState by profileViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        // Aquí se podría llamar a una función para recargar los datos del perfil
        // si fuera necesario al volver a esta pantalla.
    }

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        !uiState.isAuthenticated -> {
            NotAuthenticatedProfileScreen(onNavigateToAuth = {
                profileViewModel.signOut()
                onNavigateToAuth()
            })
        }
        else -> {
            AuthenticatedProfileScreen(
                user = uiState.user,
                rentals = uiState.userRentals,
                navController = navController,
                onSignOut = {
                    profileViewModel.signOut()
                    onNavigateToAuth()
                }
            )
        }
    }
}

@Composable
fun NotAuthenticatedProfileScreen(onNavigateToAuth: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_person),
            contentDescription = "Profile Icon",
            modifier = Modifier.size(120.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Log in to see your profile", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onNavigateToAuth,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Log In / Sign Up", fontSize = 16.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticatedProfileScreen(user: User?, rentals: List<Rental>, navController: NavController, onSignOut: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- SECCIÓN DE INFO DE USUARIO (COMPLETA Y CORREGIDA) ---
            item {
                // Foto de perfil
                Surface(
                    modifier = Modifier.size(150.dp),
                    shape = CircleShape,
                    border = BorderStroke(2.dp, Color.LightGray)
                ) {
                    AsyncImage(
                        model = user?.profilePictureUrl ?: R.drawable.profile_placeholder,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Nombre del usuario
                Text(
                    text = user?.name?.uppercase() ?: "GUEST USER",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Bio del usuario (solo si existe)
                if (!user?.bio.isNullOrBlank()) {
                    Surface(color = Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp)) {
                        Text(
                            text = user.bio,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Rating de estrellas (de ejemplo)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    (1..5).forEach {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107) // Estrella amarilla
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("5/5", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Botón de Información Personal
                Button(onClick = { navController.navigate("documents") }) {
                    Text("Información personal")
                }
                Spacer(modifier = Modifier.height(32.dp))
                Divider()
            }

            // --- SECCIÓN DE RENTAS ---
            item {
                Text(
                    text = "My Rentals",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            if (rentals.isEmpty()) {
                item {
                    Text("You haven't rented any cars yet.")
                }
            } else {
                items(rentals) { rental ->
                    RentalHistoryItem(rental = rental, navController = navController)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // --- BOTÓN DE CERRAR SESIÓN ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = onSignOut, modifier = Modifier.fillMaxWidth()) {
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalHistoryItem(rental: Rental, navController: NavController) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { navController.navigate("car_detail/${rental.carId}") }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = rental.carImageUrl,
                contentDescription = rental.carName,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(rental.carName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("From: ${dateFormat.format(rental.startDate.toDate())}", fontSize = 14.sp, color = Color.Gray)
                Text(
                    String.format(Locale.US, "$%.2f mx", rental.totalPrice),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
