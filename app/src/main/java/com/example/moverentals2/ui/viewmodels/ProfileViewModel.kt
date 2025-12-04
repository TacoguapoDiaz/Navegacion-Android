package com.example.moverentals2.ui.viewmodels

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


data class ProfileUiState(
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val userRentals: List<Rental> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val licenseUrl: String? = null,
    val idCardUrl: String? = null
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val auth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore

    init {

        auth.addAuthStateListener { firebaseAuth ->
            _uiState.update { it.copy(isLoading = true) }
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {

                _uiState.update { it.copy(isAuthenticated = true) }
                loadAllUserData(firebaseUser.uid)
            } else {

                _uiState.update {
                    it.copy(
                        isAuthenticated = false,
                        user = null,
                        userRentals = emptyList(),
                        isLoading = false
                    )
                }
            }
        }
    }


    private fun loadAllUserData(uid: String) {
        viewModelScope.launch {
            try {

                val userDoc = firestore.collection("users").document(uid).get().await()
                val user = userDoc.toObject(User::class.java)


                val rentalsSnapshot = firestore.collection("rentals")
                    .whereEqualTo("userId", uid)
                    .orderBy("bookingDate", Query.Direction.DESCENDING)
                    .get()
                    .await()
                val rentals = rentalsSnapshot.toObjects(Rental::class.java)


                _uiState.update {
                    it.copy(
                        user = user,
                        userRentals = rentals,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Failed to load profile data.")
                }
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
