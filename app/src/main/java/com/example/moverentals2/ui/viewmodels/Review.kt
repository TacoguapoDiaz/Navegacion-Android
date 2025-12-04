package com.example.moverentals2.ui.viewmodels


import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Review(
    val id: String = "",
    val carId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userImageUrl: String? = null,
    val rating: Float = 0f,
    val title: String = "",
    val body: String = "",

    @ServerTimestamp
    val timestamp: Date? = null
)
