package com.example.moverentals2.ui.viewmodels

import com.google.firebase.firestore.PropertyName

data class Car(
    val id: String = "",
    val name: String = "",
    @get:PropertyName("rating")
    val ratingAsDouble: Double = 0.0,
    @get:PropertyName("ratingCount")
    val ratingCountAsLong: Long = 0,
    @get:PropertyName("priceNum")
    val priceNumAsDouble: Double = 0.0,
    val city: String = "",
    val owner: String = "",
    val imageUrl: String = "",
    val body: String = "",
    val brand: String = "",
    val model: String = "",
    val fuel: String = "",
    val transmission: String = "",
    val description: String = "",
    val location: String = "",
    val isFeatured: Boolean = false,
    val price: Any = "",
    val year: Any = "",
    val doors: Any = "",
    val seats: Any = ""
) {

    val priceAsString: String
        get() = price.toString()

    val yearAsString: String
        get() = year.toString()

    val doorsAsString: String
        get() = doors.toString()

    val seatsAsString: String
        get() = seats.toString()
}
