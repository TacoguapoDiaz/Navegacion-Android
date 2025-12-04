package com.example.moverentals2.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class SupportChatUiState(
    val messages: List<ChatMessage> = emptyList()
)

class SupportChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SupportChatUiState())
    val uiState = _uiState.asStateFlow()


    private val botResponses = mapOf(
        "1" to "Para subir tus documentos, ve a 'Perfil' > 'Información personal'. Allí encontrarás las opciones para tu licencia y credencial.",
        "2" to "Si tienes problemas con un pago, por favor, verifica que los datos de tu tarjeta sean correctos. Si el problema persiste, contacta a tu banco.",
        "3" to "Si una renta no aparece en tu historial, asegúrate de que la reserva fue confirmada. A veces puede tardar unos minutos en sincronizarse."
    )

    init {

        sendWelcomeMessage()
    }

    private fun sendWelcomeMessage() {
        val welcomeText = "¡Hola! Soy el asistente virtual de MoveRentals. ¿Cómo puedo ayudarte? Escribe el número de tu consulta:\n\n" +
                "1. ¿Cómo subo mis documentos?\n" +
                "2. Problemas con un pago\n" +
                "3. Mi renta no aparece"

        val welcomeMessage = ChatMessage(text = welcomeText, sender = MessageSender.BOT)
        _uiState.update { it.copy(messages = listOf(welcomeMessage)) }
    }


    fun onUserMessageSent(text: String) {

        val userMessage = ChatMessage(text = text, sender = MessageSender.USER)
        _uiState.update { it.copy(messages = it.messages + userMessage) }

        val botResponseText = botResponses[text.trim()] ?: "Lo siento, no entendí esa opción. Por favor, escribe un número válido (1, 2 o 3)."
        val botMessage = ChatMessage(text = botResponseText, sender = MessageSender.BOT)
        _uiState.update { it.copy(messages = it.messages + botMessage) }
    }
}
