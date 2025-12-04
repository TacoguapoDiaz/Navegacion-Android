package com.example.moverentals2.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moverentals2.data.SettingsManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class SettingsViewModel(application: Application) : AndroidViewModel(application) {


    private val settingsManager = SettingsManager(application)


    val isDarkMode: StateFlow<Boolean> = settingsManager.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )


    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            settingsManager.setDarkMode(isDarkMode)
        }
    }
}
