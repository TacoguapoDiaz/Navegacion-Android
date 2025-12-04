package com.example.moverentals2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moverentals2.R

// Se han eliminado las importaciones de Scaffold, TopAppBar, Icon, etc. que ya no se usan aquí.

@Composable
fun FavoritesListScreen(navController: NavController, modifier: Modifier = Modifier) {
    // Simula si hay contenido o no. Cambia a 'true' para ver la lista.
    val hasContent by remember { mutableStateOf(false) }

    // El Scaffold, TopBar y BottomBar han sido eliminados.
    // Esta pantalla ahora solo dibuja su contenido específico.
    // El 'modifier' que recibe ya contiene el padding del Scaffold principal.

    if (hasContent) {
        // Muestra la lista si hay favoritos
        LazyColumn(
            modifier = modifier // Usa el modifier que viene como parámetro
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp), // Padding para el contenido
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // NOTA: El Composable 'CarListItem' debe estar definido en alguna parte
            // para que esto funcione. Si aún no existe, estas líneas darán error.
            item {
                // CarListItem(
                //     imageRes = R.drawable.mazda_blue,
                //     carName = "Mazda 2025",
                //     rating = 4.5,
                //     isFavorite = true
                // )
            }
            item {
                // CarListItem(
                //     imageRes = R.drawable.nissan_versa_blue,
                //     carName = "Versa 2024",
                //     rating = 4.0,
                //     isFavorite = true
                // )
            }
        }
    } else {
        // Muestra el estado vacío si no hay favoritos
        // NOTA: El Composable 'EmptyState' debe estar definido para que esto funcione.
        EmptyState(
            modifier = modifier.fillMaxSize(), // Usa el modifier que viene como parámetro
            title = stringResource(R.string.no_favorites_yet),
            subtitle = stringResource(R.string.no_favorites_yet_desc),
            buttonText = stringResource(R.string.start_exploring),
            onButtonClick = { navController.navigate("car_list") }
        )
    }
}
