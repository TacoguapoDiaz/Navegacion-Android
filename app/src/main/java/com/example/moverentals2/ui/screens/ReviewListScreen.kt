package com.example.moverentals2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moverentals2.R
import com.example.moverentals2.ui.viewmodels.CarDetailViewModel
import com.example.moverentals2.ui.viewmodels.Review
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewListScreen(
    navController: NavController,
    carDetailViewModel: CarDetailViewModel
) {
    val state by carDetailViewModel.uiState.collectAsState()
    val reviews = state.reviews

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Reviews", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Navega a la pantalla de añadir review, asegurándose de que el carId no sea nulo
                state.car?.id?.let { carId ->
                    navController.navigate("add_review/${carId}")
                }
            }) {
                Icon(painterResource(id = R.drawable.ic_more_dots), contentDescription = "Add Review",modifier = Modifier.size(24.dp))
            }
        }
    ) { innerPadding ->
        if (reviews.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No reviews yet. Be the first!")
            }
        } else {
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(reviews) { review ->
                    ReviewItem(review = review)
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.weight(1f)) {
                    (1..5).forEach { index ->
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = if (index <= review.rating.toInt()) Color(0xFFFFC107) else Color.LightGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                // --- LÍNEA CORREGIDA ---
                // Se elimina el ".toDate()" y se añade una comprobación de nulos "?."
                Text(
                    text = review.timestamp?.let { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it) } ?: "",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                // --- FIN DE LA CORRECCIÓN ---
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(review.title, fontWeight = FontWeight.Bold)
            Text(review.body, fontSize = 14.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = review.userImageUrl ?: R.drawable.profile_placeholder,
                    contentDescription = "Reviewer",
                    modifier = Modifier.size(30.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.profile_placeholder) // Fallback
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(review.userName, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }
}
