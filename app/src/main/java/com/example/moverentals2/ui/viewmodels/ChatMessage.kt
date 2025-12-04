package com.example.moverentals2.ui.viewmodels

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date


enum class MessageSender {
    USER, BOT
}


data class ChatMessage(
    val text: String = "",
    val sender: MessageSender = MessageSender.USER,
    @ServerTimestamp val timestamp: Date = Date()
)
