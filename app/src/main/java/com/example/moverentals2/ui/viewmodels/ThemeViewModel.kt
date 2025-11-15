package com.example.moverentals2.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moverentals2.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsDataStore = SettingsDataStore(application)

    // Expone el estado del modo oscuro como un StateFlow
    val isDarkMode: StateFlow<Boolean> = settingsDataStore.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // Función para cambiar el estado
    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setDarkMode(isDark)
        }
    }
}
    