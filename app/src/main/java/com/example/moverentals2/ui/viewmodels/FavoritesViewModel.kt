package com.example.moverentals2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class FavoritesUiState(
    val favoriteCars: List<Car> = emptyList(),
    val favoriteCarIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class FavoritesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState = _uiState.asStateFlow()

    private val firestore = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()


    private val userDocRef
        get() = auth.currentUser?.uid?.let { firestore.collection("users").document(it) }

    init {
        observeFavoriteIds()
    }

    private fun observeFavoriteIds() {
        val currentUser = auth.currentUser
        if (currentUser == null) return

        viewModelScope.launch {

            firestore.collection("users").document(currentUser.uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) return@addSnapshotListener

                    if (snapshot != null && snapshot.exists()) {

                        val ids = snapshot.get("favoriteCarIds") as? List<String> ?: emptyList()
                        _uiState.update { it.copy(favoriteCarIds = ids.toSet()) }


                        loadFavoriteCars(ids)
                    }
                }
        }
    }

    private fun loadFavoriteCars(ids: List<String>) {
        viewModelScope.launch {
            if (ids.isEmpty()) {
                _uiState.update { it.copy(favoriteCars = emptyList(), isLoading = false) }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }
            try {

                val chunks = ids.chunked(10)
                val allCars = mutableListOf<Car>()

                for (chunk in chunks) {

                    val carsSnapshot = firestore.collection("cars")
                        .whereIn(FieldPath.documentId(), chunk)
                        .get().await()
                    allCars.addAll(carsSnapshot.toObjects(Car::class.java))
                }

                _uiState.update { it.copy(favoriteCars = allCars, isLoading = false) }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to load favorites.") }
            }
        }
    }

    fun toggleFavorite(carId: String) {
        if (carId.isEmpty()) return

        viewModelScope.launch {
            val docRef = userDocRef ?: return@launch
            val isCurrentlyFavorite = _uiState.value.favoriteCarIds.contains(carId)

            val updateTask = if (isCurrentlyFavorite) {
                docRef.update("favoriteCarIds", FieldValue.arrayRemove(carId))
            } else {
                docRef.update("favoriteCarIds", FieldValue.arrayUnion(carId))
            }

            try {
                updateTask.await()
            } catch (e: Exception) {

                if (e.message?.contains("NOT_FOUND") == true || !isCurrentlyFavorite) {
                    try {
                        val data = mapOf("favoriteCarIds" to listOf(carId))
                        docRef.set(data, com.google.firebase.firestore.SetOptions.merge()).await()
                    } catch (creationError: Exception) {
                        creationError.printStackTrace()
                    }
                }
            }
        }
    }
}