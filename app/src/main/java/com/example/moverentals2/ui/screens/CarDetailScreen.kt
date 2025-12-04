package com.example.moverentals2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moverentals2.R
import com.example.moverentals2.ui.viewmodels.Car
import com.example.moverentals2.ui.viewmodels.CarDetailViewModel
import com.example.moverentals2.ui.viewmodels.Review
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale
import kotlin.collections.first
import kotlin.collections.isNotEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(
    navController: NavController,
    carDetailViewModel: CarDetailViewModel = viewModel()
) {
    val uiState by carDetailViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            uiState.car?.let { car ->
                CarDetailBottomBar(car = car, navController = navController)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage ?: "An unknown error occurred.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.car != null -> {
                    val car = uiState.car!!
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item { CarImageHeader(car = car, navController = navController) }
                        item { CarSpecsSection(car = car) }
                        item { PickUpLocationSection(car = car) }
                        item {
                            Button(
                                onClick = {navController.navigate("rental_conditions") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.LightGray.copy(
                                        alpha = 0.5f
                                    )
                                )
                            ) {
                                Text("Rental Conditions", color = Color.Black)
                            }
                        }
                        item { ReviewsSection(car = car, reviews = uiState.reviews, navController = navController) }
                    }
                }
            }
        }
    }
}

@Composable
private fun CarImageHeader(car: Car, navController: NavController) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1.5f)) {
        AsyncImage(
            model = car.imageUrl,
            contentDescription = car.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .background(Color.White.copy(alpha = 0.6f), CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
        }
    }
}

@Composable
private fun CarSpecsSection(car: Car) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(car.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        CarSpecItem(iconRes = R.drawable.ic_door, text = car.body)
        CarSpecItem(iconRes = R.drawable.ic_tag, text = car.transmission)
        // --- CORRECCIÓN 1 ---
        CarSpecItem(iconRes = R.drawable.ic_star, text = String.format(Locale.US, "%.1f", car.ratingAsDouble))
        CarSpecItem(iconRes = R.drawable.ic_phone, text = "Contact with the Owner")
    }
}

@Composable
private fun CarSpecItem(iconRes: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 16.sp)
    }
}

@Composable
private fun PickUpLocationSection(car: Car) {
    val carLocationCoordinates = LatLng(21.9084, -102.2541)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(carLocationCoordinates, 15f)
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(id = R.drawable.ic_location),
                contentDescription = "Pick Up",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Pick Up",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            car.location,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp)),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = carLocationCoordinates),
                title = "Punto de Recogida",
                snippet = car.name
            )
        }
    }
}

@Composable
private fun ReviewsSection(car: Car, reviews: List<Review>, navController: NavController) {
    var showConditionsDialog by remember { mutableStateOf(false) }

    if (showConditionsDialog) {
        AlertDialog(
            onDismissRequest = { showConditionsDialog = false },
            title = { Text("Rental Conditions") },
            text = { Text("Si lo chocas pagas tu we") },
            confirmButton = {
                TextButton(onClick = { showConditionsDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.weight(1f)) {
                (1..5).forEach { index ->
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        // --- CORRECCIÓN 2 ---
                        tint = if (index <= car.ratingAsDouble.toInt()) Color(0xFFFFC107) else Color.LightGray
                    )
                }
            }
            OutlinedButton(
                onClick = { navController.navigate("reviews/${car.id}") },
                shape = CircleShape,
            ) {
                Icon(painterResource(id = R.drawable.ic_chat), contentDescription = "See All Reviews",modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${reviews.size} Reviews")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (reviews.isNotEmpty()) {
            ReviewItem(review = reviews.first())
        } else {
            Text("No reviews yet for this car.", modifier = Modifier.padding(vertical = 16.dp))
        }
    }
}

@Composable
private fun CarDetailBottomBar(car: Car, navController: NavController) {
    Surface(shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "${car.price}mx",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                Text("per day", fontSize = 14.sp, color = Color.Gray)
            }
            Button(
                onClick = { navController.navigate("order_summary/${car.id}") },
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Rent", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}
