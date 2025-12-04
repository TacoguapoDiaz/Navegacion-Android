package com.example.moverentals2.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moverentals2.data.RecentCarsManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class RecentsUiState(
    val recentCars: List<Car> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class RecentsViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RecentsUiState())
    val uiState = _uiState.asStateFlow()

    private val firestore = Firebase.firestore


    fun loadRecentCars() {
        viewModelScope.launch {

            val recentIds = RecentCarsManager.getRecentCarIds(getApplication())

            if (recentIds.isEmpty()) {
                _uiState.update { it.copy(recentCars = emptyList(), isLoading = false) }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }
            try {

                val carsSnapshot = firestore.collection("cars")
                    .whereIn("id", recentIds)
                    .get().await()

                var cars = carsSnapshot.toObjects(Car::class.java)


                cars = cars.sortedBy { recentIds.indexOf(it.id) }

                _uiState.update { it.copy(recentCars = cars, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to load recent cars.") }
            }
        }
    }
}
