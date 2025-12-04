package com.example.moverentals2.ui.viewmodels

import com.google.firebase.Timestamp

data class Rental(
    val id: String = "",
    val userId: String = "",
    val carId: String = "",
    val carName: String = "",
    val carImageUrl: String = "",
    val startDate: Timestamp = Timestamp.now(),
    val endDate: Timestamp = Timestamp.now(),
    val totalPrice: Double = 0.0,
    val bookingDate: Timestamp = Timestamp.now()
)
