package com.example.moverentals2.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext // <-- PASO 1: Importar LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moverentals2.ui.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                SettingItem(
                    icon = Icons.Default.SupportAgent,
                    text = "Llamar a Soporte",
                    onClick = {

                        val phoneNumber = "+1234567890"
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:$phoneNumber")
                        }
                        context.startActivity(intent)
                    }
                )
            }


            item {
                SettingItem(
                    icon = Icons.Default.DarkMode,
                    text = "Modo oscuro",
                    hasSwitch = true,
                    isSwitchedOn = isDarkMode,
                    onSwitchChange = { newValue ->
                        settingsViewModel.setDarkMode(newValue)
                    },
                    onClick = {
                        settingsViewModel.setDarkMode(!isDarkMode)
                    }
                )
            }
            item {
                SettingItem(
                    icon = Icons.Default.Translate,
                    text = "Idioma",
                    onClick = { navController.navigate("language") }
                )
            }
            item {
                SettingItem(
                    icon = Icons.Default.AttachMoney,
                    text = "Moneda",
                    onClick = { navController.navigate("currency") }
                )
            }
        }
    }
}

@Composable
private fun SettingItem(
    icon: ImageVector,
    text: String,
    hasSwitch: Boolean = false,
    isSwitchedOn: Boolean = false,
    onSwitchChange: (Boolean) -> Unit = {},
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = text, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            if (hasSwitch) {
                Switch(checked = isSwitchedOn, onCheckedChange = onSwitchChange)
            } else {
                Icon(Icons.Default.ChevronRight, contentDescription = null)
            }
        }
    }
}
