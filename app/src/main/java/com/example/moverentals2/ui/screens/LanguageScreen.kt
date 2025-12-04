package com.example.moverentals2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // <-- IMPORTACIÓN CORREGIDA
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moverentals2.R
// Asegúrate de que la importación de tu TopBar sea correcta.
// import com.example.moverentals2.ui.shared.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(navController: NavController) {
    // Asumiendo que tienes un composable TopBar definido en alguna parte
    // Si "TopBar" sigue marcando error, es porque no está definido o importado.
    // Podrías reemplazarlo temporalmente con: title = { Text("Language") } en un TopAppBar
    Scaffold(topBar = { TopBar(navController, title = stringResource(id = R.string.language)) }) { innerPadding ->
        // Lista de los recursos de string para cada idioma
        val languages = listOf(
            R.string.spanish,
            R.string.english,
            R.string.french,
            R.string.russian,
            R.string.japanese,
            R.string.chinese
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Ahora esta línea no debería dar error
            items(languages) { languageResId ->
                LanguageButton(
                    language = stringResource(id = languageResId),
                    onClick = {
                        // Lógica para cambiar idioma
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
private fun LanguageButton(language: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(
            text = language,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

