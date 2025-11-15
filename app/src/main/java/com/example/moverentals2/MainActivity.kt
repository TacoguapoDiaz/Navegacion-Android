package com.example.moverentals2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.moverentals2.ui.AppNavigation
import com.example.moverentals2.ui.theme.MoveRentals2Theme
import com.example.moverentals2.ui.viewmodels.ThemeViewModel

class MainActivity : ComponentActivity() {
    // Obtenemos una instancia del ViewModel
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Recolectamos el estado del modo oscuro desde el ViewModel
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            // Le pasamos el estado a nuestro tema principal
            MoveRentals2Theme(darkTheme = isDarkMode) {
                AppNavigation(themeViewModel = themeViewModel)
            }
        }
    }
}
    