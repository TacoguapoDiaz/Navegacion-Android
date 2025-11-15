package com.example.moverentals2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moverentals2.R
import com.example.moverentals2.ui.screensimport.TopBar

// --- CORRECCIÓN: Se elimina la importación incorrecta de TopBar ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(navController: NavController) {
    Scaffold(topBar = { TopBar(navController, stringResource(id = R.string.support), Color(0xFFFFCDB2)) }) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                // --- CORRECCIÓN: Se implementa el contenido de la lista ---
                item { SupportItem(text = "No se refleja el pago") }
                item { SupportItem(text = "No tengo contacto con el cliente") }
                item { SupportItem(text = "Reclamo") }
                item { SupportItem(text = "No me han entregado el auto") }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painterResource(id = R.drawable.ic_headset), null, modifier = Modifier.size(48.dp))
                Text(stringResource(id = R.string.contact_us), fontWeight = FontWeight.Bold)
                Text("449 584 45 67")
            }
        }
    }
}

// --- CORRECCIÓN: Se implementa el Composable SupportItem ---
@Composable
fun SupportItem(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(id = R.drawable.ic_star), contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text)
            }
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Expand")
        }
    }
}
