package com.example.moverentals2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moverentals2.R
import com.example.moverentals2.ui.screens.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersScreen(navController: NavController) {
    Scaffold(topBar = { TopBar(navController, stringResource(id = R.string.offers)) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CarCard(
                    navController = navController,
                    imageRes = R.drawable.nissan_versa,
                    carName = "Versa 2020",
                    rating = 4.5,
                    price = 800,
                    city = "Aguascalientes",
                    owner = "Peter",
                    backgroundColor = Color(0xFF8ECAE6) // Azul claro
                )
            }
            item {
                CarCard(
                    navController = navController,
                    imageRes = R.drawable.mazda_5cx,
                    carName = "Mazda 5cx",
                    rating = 3.8,
                    price = 650,
                    city = "Aguascalientes",
                    owner = "Macario",
                    backgroundColor = Color.White
                )
            }
        }
    }
}
