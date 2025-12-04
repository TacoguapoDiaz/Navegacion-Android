package com.example.moverentals2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class HomeUiState(
    val cars: List<Car> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val firestore = Firebase.firestore

    init {
        loadCars()
    }

    fun loadCars(
        priceMin: Float? = null,
        priceMax: Float? = null,
        ratingMin: Float? = null
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                var query: Query = firestore.collection("cars")


                priceMin?.let { query = query.whereGreaterThanOrEqualTo("priceNum", it.toDouble()) }
                priceMax?.let { if(it < 10000f) query = query.whereLessThanOrEqualTo("priceNum", it.toDouble()) }
                ratingMin?.let { query = query.whereGreaterThanOrEqualTo("rating", it.toDouble()) }

                val snapshot = query.get().await()

                val cars = snapshot.toObjects(Car::class.java)

                if (cars.isEmpty() && !snapshot.isEmpty) {

                    _uiState.update { it.copy(isLoading = false, errorMessage = "Error: Verifica los tipos de datos en Firebase (Números vs Strings).") }
                } else {
                    _uiState.update { it.copy(cars = cars, isLoading = false) }
                }

            } catch (e: Exception) {
                e.printStackTrace()

                _uiState.update { it.copy(isLoading = false, errorMessage = "Error loading cars: ${e.message}") }
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}