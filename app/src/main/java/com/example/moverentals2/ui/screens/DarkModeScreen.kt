package com.example.moverentals2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moverentals2.R
import com.example.moverentals2.ui.screensimport.TopBar
import com.example.moverentals2.ui.viewmodels.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DarkModeScreen(navController: NavController, themeViewModel: ThemeViewModel) {
    val isDarkMode by themeViewModel.isDarkMode.collectAsState()

    Scaffold(topBar = { TopBar(navController, stringResource(id = R.string.dark_mode), Color(0xFFFFCDB2)) }) { innerPadding ->
        Row(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(id = R.string.activate))
            Switch(checked = isDarkMode, onCheckedChange = { themeViewModel.setDarkMode(it) })
        }
    }
}
