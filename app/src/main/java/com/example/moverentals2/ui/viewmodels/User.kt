package com.example.moverentals2.ui.viewmodels

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profilePictureUrl: String? = null,
    val bio: String = "Welcome to my profile!",
    val idDocumentUrl: String? = null,
    val licenseUrl: String? = null,
    @ServerTimestamp val createdAt: Date? = null
)
