package com.example.moverentals2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(navController: NavController) {

    // --- ¡AQUÍ ESTÁ LA LÓGICA DE TIEMPO! ---
    // Este efecto se ejecutará una sola vez cuando la pantalla aparezca.
    LaunchedEffect(key1 = true) {
        delay(5000) // Espera 5 segundos (5000 milisegundos)
        navController.navigate("login") {
            // Esta opción limpia la pila de navegación para que el usuario
            // no pueda volver a la pantalla de carga con el botón de "atrás".
            popUpTo("loading") {
                inclusive = true
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Placeholder para la animación de círculos
        Box(
            modifier = Modifier
                .size(150.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
        }

        Text(
            text = "LOADING...",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp)
        )
    }
}
