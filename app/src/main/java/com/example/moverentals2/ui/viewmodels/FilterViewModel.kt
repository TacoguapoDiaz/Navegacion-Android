package com.example.moverentals2.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FilterUiState(

    val priceRange: ClosedFloatingPointRange<Float> = 0f..10000f,

    val ratingRange: ClosedFloatingPointRange<Float> = 0f..5f
)

class FilterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState = _uiState.asStateFlow()

    fun setPriceRange(newRange: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(priceRange = newRange) }
    }

    fun setRatingRange(newRange: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(ratingRange = newRange) }
    }
}
