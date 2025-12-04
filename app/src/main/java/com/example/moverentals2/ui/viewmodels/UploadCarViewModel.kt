package com.example.moverentals2.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.random.Random

data class UploadCarUiState(
    val selectedImageUri: Uri? = null,
    val isUploading: Boolean = false,
    val uploadSuccess: Boolean = false,
    val errorMessage: String? = null,


    val price: String = "",
    val city: String = "",
    val description: String = "",
    val location: String = "",


    val bodyOptions: List<String> = listOf("Sedan", "SUV", "Hatchback", "Coupe", "Convertible", "Pickup"),
    val selectedBody: String = "Body",

    val yearOptions: List<String> = (2025 downTo 2000).map { it.toString() },
    val selectedYear: String = "Year",

    val brandOptions: List<String> = listOf("Nissan", "Mazda", "Toyota", "Honda", "Ford", "Chevrolet", "BMW", "Audi"),
    val selectedBrand: String = "Brand",

    val modelOptions: List<String> = listOf("Versa", "Sentra", "CX-5", "Corolla", "Civic", "Mustang", "Aveo"), // Ejemplo simplificado
    val selectedModel: String = "Model",

    val fuelOptions: List<String> = listOf("Gasoline", "Diesel", "Electric", "Hybrid"),
    val selectedFuel: String = "Fuel",

    val transmissionOptions: List<String> = listOf("Automatic", "Manual"),
    val selectedTransmission: String = "Transmission",

    val doorsOptions: List<String> = listOf("2 Doors", "3 Doors", "4 Doors", "5 Doors"),
    val selectedDoors: String = "Doors",

    val seatsOptions: List<String> = listOf("2 Seats", "4 Seats", "5 Seats", "7 Seats", "8+ Seats"),
    val selectedSeats: String = "Seats"
)

class UploadCarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UploadCarUiState())
    val uiState = _uiState.asStateFlow()

    private val storage = Firebase.storage
    private val firestore = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()


    fun publishCar() {
        viewModelScope.launch {
            val currentState = _uiState.value

            if (currentState.selectedImageUri == null) {
                _uiState.update { it.copy(errorMessage = "Please select an image") }
                return@launch
            }
            if (currentState.selectedBrand == "Brand" ||
                currentState.selectedYear == "Year" ||
                currentState.price.isBlank() ||
                currentState.city.isBlank() ||
                currentState.selectedDoors == "Doors" ||
                currentState.selectedSeats == "Seats") {

                _uiState.update { it.copy(errorMessage = "Please fill all fields correctly") }
                return@launch
            }

            _uiState.update { it.copy(isUploading = true) }

            try {

                val imageUrl = uploadImageToStorage(currentState.selectedImageUri)


                val priceNumber = currentState.price.replace(",", "").toDoubleOrNull() ?: 0.0
                val carId = UUID.randomUUID().toString()


                val carData = mapOf(
                    "id" to carId,
                    "name" to "${currentState.selectedYear} ${currentState.selectedBrand} ${currentState.selectedModel}",
                    "rating" to Random.nextDouble(3.5, 5.0),
                    "ratingCount" to 0L,


                    "price" to currentState.price,
                    "priceNum" to priceNumber,

                    "city" to currentState.city,
                    "owner" to (auth.currentUser?.uid ?: ""),
                    "imageUrl" to imageUrl,
                    "isFeatured" to false,
                    "body" to currentState.selectedBody,
                    "year" to currentState.selectedYear,
                    "brand" to currentState.selectedBrand,
                    "model" to currentState.selectedModel,
                    "fuel" to currentState.selectedFuel,
                    "transmission" to currentState.selectedTransmission,
                    "doors" to currentState.selectedDoors,
                    "seats" to currentState.selectedSeats,
                    "description" to currentState.description,
                    "location" to currentState.location
                )


                firestore.collection("cars").document(carId).set(carData).await()
                _uiState.update { it.copy(isUploading = false, uploadSuccess = true) }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isUploading = false, errorMessage = e.message) }
            }
        }
    }



    private suspend fun uploadImageToStorage(imageUri: Uri): String {
        val storageRef = storage.reference.child("car_images/${UUID.randomUUID()}")
        storageRef.putFile(imageUri).await()
        return storageRef.downloadUrl.await().toString()
    }


    fun onPriceChanged(newPrice: String) {
        if (newPrice.matches(Regex("^\\d*\\.?\\d*\$"))) {
            _uiState.update { it.copy(price = newPrice) }
        }
    }
    fun onCityChanged(newCity: String) { _uiState.update { it.copy(city = newCity) } }
    fun onLocationChanged(newLoc: String) { _uiState.update { it.copy(location = newLoc) } }
    fun onDescriptionChanged(desc: String) { _uiState.update { it.copy(description = desc) } }

    // Dropdowns
    fun onImageSelected(uri: Uri?) { _uiState.update { it.copy(selectedImageUri = uri) } }
    fun onBodySelected(body: String) { _uiState.update { it.copy(selectedBody = body) } }
    fun onYearSelected(year: String) { _uiState.update { it.copy(selectedYear = year) } }
    fun onBrandSelected(brand: String) { _uiState.update { it.copy(selectedBrand = brand) } }
    fun onModelSelected(model: String) { _uiState.update { it.copy(selectedModel = model) } }
    fun onFuelSelected(fuel: String) { _uiState.update { it.copy(selectedFuel = fuel) } }
    fun onTransmissionSelected(trans: String) { _uiState.update { it.copy(selectedTransmission = trans) } }

    fun onDoorsSelected(doors: String) { _uiState.update { it.copy(selectedDoors = doors) } }
    fun onSeatsSelected(seats: String) { _uiState.update { it.copy(selectedSeats = seats) } }

    fun dismissError() { _uiState.update { it.copy(errorMessage = null) } }
}
