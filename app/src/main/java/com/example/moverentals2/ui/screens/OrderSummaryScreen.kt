package com.example.moverentals2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.moverentals2.ui.viewmodels.OrderViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = viewModel()
) {
    val uiState by orderViewModel.uiState.collectAsState()
    val showDatePicker = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Summary") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Go back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD32F2F),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFADD8E6) // Fondo azul claro
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            // Imagen del coche
            AsyncImage(
                model = uiState.car?.imageUrl,
                contentDescription = uiState.car?.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Selector de fecha y precio
            InfoRow(label = "Selected Days", value = uiState.car?.let { "${uiState.totalDays} days" } ?: "N/A", isAction = true) {
                showDatePicker.value = true
            }
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(label = "Total Price", value = String.format(Locale.US, "$%.2f mx", uiState.totalPrice), isAction = false) {}

            Spacer(modifier = Modifier.weight(1f))

            // Total y botón de pagar
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = String.format(Locale.US, "$%.2f mx", uiState.totalPrice),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("payment_method/${uiState.car?.id}") },
                    enabled = uiState.startDate != null && uiState.endDate != null,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFA8072)) // Salmón
                ) {
                    Text("Pay", fontSize = 18.sp, color = Color.White)
                }
            }
        }

        if (showDatePicker.value) {
            val dateRangePickerState = rememberDateRangePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDatePicker.value = false
                            val start = dateRangePickerState.selectedStartDateMillis?.let { Date(it) }
                            val end = dateRangePickerState.selectedEndDateMillis?.let { Date(it) }
                            if (start != null && end != null) {
                                orderViewModel.setDates(start, end)
                            }
                        },
                        enabled = dateRangePickerState.selectedEndDateMillis != null
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker.value = false }) { Text("Cancel") }
                }
            ) {
                DateRangePicker(state = dateRangePickerState, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, isAction: Boolean, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        // Label Box
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .background(Color.White, RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text(label, fontWeight = FontWeight.SemiBold)
        }
        // Value Box
        Button(
            onClick = onClick,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isAction) Color(0xFFFA8072) else Color(0xFFF08080) // Salmón claro/oscuro
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Text(value, color = Color.White)
        }
    }
}
