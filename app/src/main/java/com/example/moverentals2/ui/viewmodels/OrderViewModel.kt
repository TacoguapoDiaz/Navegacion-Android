package com.example.moverentals2.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit

data class OrderUiState(
    val car: Car? = null,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val totalDays: Long = 0,
    val totalPrice: Double = 0.0,
    val isLoading: Boolean = true,
    val isProcessingPayment: Boolean = false,
    val rentalSuccess: Boolean = false,
    val errorMessage: String? = null
)

class OrderViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState = _uiState.asStateFlow()

    private val firestore = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    private val carId: String = savedStateHandle.get<String>("carId").orEmpty()

    init {
        loadCar()
    }

    private fun loadCar() {
        if (carId.isEmpty()) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Car ID not found.") }
            return
        }
        viewModelScope.launch {
            try {
                val carDoc = firestore.collection("cars").document(carId).get().await()
                val car = carDoc.toObject(Car::class.java)
                _uiState.update { it.copy(car = car, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to load car details: ${e.message}") }
            }
        }
    }

    fun setDates(startDate: Date, endDate: Date) {
        val diff = endDate.time - startDate.time
        val days = TimeUnit.MILLISECONDS.toDays(diff).coerceAtLeast(1)
        val priceString = _uiState.value.car?.priceAsString ?: "0"
        val cleanedPriceString = priceString.replace(Regex("[^0-9.]"), "")
        val carPrice = cleanedPriceString.toDoubleOrNull() ?: 0.0


        val totalPrice = days * carPrice

        _uiState.update {
            it.copy(
                startDate = startDate,
                endDate = endDate,
                totalDays = days,
                totalPrice = totalPrice
            )
        }
    }

    fun processRental() {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessingPayment = true) }
            val state = _uiState.value
            val user = auth.currentUser
            if (user == null || state.car == null || state.startDate == null || state.endDate == null) {
                _uiState.update { it.copy(errorMessage = "Missing required information.", isProcessingPayment = false) }
                return@launch
            }

            try {
                val newRental = Rental(
                    id = UUID.randomUUID().toString(),
                    userId = user.uid,
                    carId = state.car.id,
                    carName = state.car.name,
                    carImageUrl = state.car.imageUrl,
                    startDate = com.google.firebase.Timestamp(state.startDate),
                    endDate = com.google.firebase.Timestamp(state.endDate),
                    totalPrice = state.totalPrice
                )

                kotlinx.coroutines.delay(2000)
                firestore.collection("rentals").document(newRental.id).set(newRental).await()

                _uiState.update { it.copy(isProcessingPayment = false, rentalSuccess = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isProcessingPayment = false, errorMessage = "Failed to process rental: ${e.message}") }
            }
        }
    }
}
