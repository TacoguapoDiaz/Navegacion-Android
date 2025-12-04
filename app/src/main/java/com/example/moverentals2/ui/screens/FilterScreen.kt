package com.example.moverentals2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moverentals2.R
import com.example.moverentals2.ui.viewmodels.FilterViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavController,
    filterViewModel: FilterViewModel = viewModel()
) {
    val uiState by filterViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filters", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painterResource(id = R.drawable.ic_arrow_back), "Go Back")
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Filtro de Precio
            FilterSlider(
                title = "Price per day",
                range = uiState.priceRange,
                onRangeChange = { filterViewModel.setPriceRange(it) },
                valueRange = 0f..10000f,
                steps = 19, // (10000 / 500) - 1
                format = { value -> String.format(Locale.US, "$%.0f", value) }
            )

            // Filtro de Calificación
            FilterSlider(
                title = "Rating",
                range = uiState.ratingRange,
                onRangeChange = { filterViewModel.setRatingRange(it) },
                valueRange = 0f..5f,
                steps = 4, // 5 estrellas
                format = { value -> String.format(Locale.US, "%.1f ★", value) }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón para aplicar filtros
            Button(
                onClick = {
                    // Pasamos los filtros de vuelta a la pantalla anterior
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("price_min", uiState.priceRange.start)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("price_max", uiState.priceRange.endInclusive)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("rating_min", uiState.ratingRange.start)

                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Apply Filters", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSlider(
    title: String,
    range: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    format: (Float) -> String
) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        RangeSlider(
            value = range,
            onValueChange = onRangeChange,
            valueRange = valueRange,
            steps = steps,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = format(range.start))
            Text(text = format(range.endInclusive))
        }
    }
}
