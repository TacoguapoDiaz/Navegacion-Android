package com.example.moverentals2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.moverentals2.ui.navigation.AppNavigation
import com.example.moverentals2.ui.theme.MoveRentals2Theme
import com.example.moverentals2.ui.viewmodels.SettingsViewModel
import com.example.moverentals2.ui.viewmodels.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val settingsViewModel: SettingsViewModel by viewModels()

        setContent {

            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

            MoveRentals2Theme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation() // Tu sistema de navegación
                }
            }
        }
    }
}
