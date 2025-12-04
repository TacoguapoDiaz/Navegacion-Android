package com.example.moverentals2.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moverentals2.ui.viewmodels.CarDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewScreen(
    navController: NavController,
    carDetailViewModel: CarDetailViewModel
) {
    val uiState by carDetailViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var rating by remember { mutableStateOf(0) }
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    // --- USA LOS NOMBRES DEL VIEWMODEL CORREGIDO ---
    LaunchedEffect(uiState.reviewSubmitted) {
        if (uiState.reviewSubmitted) {
            Toast.makeText(context, "Review published!", Toast.LENGTH_SHORT).show()
            carDetailViewModel.resetReviewSubmissionStatus() // Limpiar el estado
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Review", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.padding(vertical = 16.dp)) {
                (1..5).forEach { index ->
                    IconButton(onClick = { rating = index }) {
                        Icon(
                            imageVector = if (index <= rating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = "Rate $index",
                            tint = if (index <= rating) Color(0xFFFFC107) else Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Review Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text("Write your experience...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text("Cancel", color = Color.White)
                }
                Button(
                    onClick = {
                        carDetailViewModel.submitReview(rating.toFloat(), title, body)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    // --- USA LOS NOMBRES DEL VIEWMODEL CORREGIDO ---
                    enabled = !uiState.isSubmittingReview && rating > 0 && body.isNotBlank()
                ) {
                    if (uiState.isSubmittingReview) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Publish")
                    }
                }
            }
        }
    }
}
