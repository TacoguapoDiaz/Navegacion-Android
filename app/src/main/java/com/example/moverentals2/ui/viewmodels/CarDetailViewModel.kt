package com.example.moverentals2.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID


data class CarDetailUiState(
    val car: Car? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,

    val reviews: List<Review> = emptyList(),
    val isSubmittingReview: Boolean = false,
    val reviewSubmitted: Boolean = false
)

class CarDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val firestore = Firebase.firestore

    private val auth = FirebaseAuth.getInstance()
    private val carId: String = savedStateHandle.get<String>("carId").orEmpty()

    init {
        if (carId.isEmpty()) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Car ID is missing.") }
        } else {
            loadCarAndReviews()
        }
    }

    private fun loadCarAndReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val carDocument = firestore.collection("cars").document(carId).get().await()
                val carData = if (carDocument.exists()) carDocument.toObject(Car::class.java) else null

                val reviewsSnapshot = firestore.collection("reviews")
                    .whereEqualTo("carId", carId)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get().await()
                val reviewsData = reviewsSnapshot.toObjects(Review::class.java)

                _uiState.update { it.copy(car = carData, reviews = reviewsData, isLoading = false) }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to load details.") }
            }
        }
    }


    fun submitReview(rating: Float, title: String, body: String) {
        viewModelScope.launch {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                _uiState.update { it.copy(errorMessage = "You must be logged in to post a review.") }
                return@launch
            }

            _uiState.update { it.copy(isSubmittingReview = true) }

            try {
                val reviewId = UUID.randomUUID().toString()
                val newReview = Review(
                    id = reviewId,
                    carId = carId,
                    userId = currentUser.uid,
                    userName = currentUser.displayName ?: "Anonymous User",
                    userImageUrl = currentUser.photoUrl?.toString(),
                    rating = rating,
                    title = title,
                    body = body,
                )

                firestore.collection("reviews").document(reviewId).set(newReview).await()

                val carRef = firestore.collection("cars").document(carId)
                firestore.runTransaction { transaction ->
                    val carSnapshot = transaction.get(carRef)
                    val currentRatingCount = carSnapshot.getDouble("ratingCount") ?: 0.0
                    val currentAverageRating = carSnapshot.getDouble("rating") ?: 0.0
                    val newRatingCount = currentRatingCount + 1
                    val newAverageRating = ((currentAverageRating * currentRatingCount) + rating) / newRatingCount
                    transaction.update(carRef, "rating", newAverageRating)
                    transaction.update(carRef, "ratingCount", newRatingCount)
                }.await()


                loadCarAndReviews()

                _uiState.update { it.copy(isSubmittingReview = false, reviewSubmitted = true) }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isSubmittingReview = false, errorMessage = "Failed to submit review: ${e.message}") }
            }
        }
    }


    fun resetReviewSubmissionStatus() {
        _uiState.update { it.copy(reviewSubmitted = false) }
    }
}
