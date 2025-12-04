package com.example.moverentals2.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration // 1. Import para detectar la orientación
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moverentals2.ui.viewmodels.ChatMessage
import com.example.moverentals2.ui.viewmodels.MessageSender
import com.example.moverentals2.ui.viewmodels.SupportChatViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportChatScreen(
    navController: NavController,
    viewModel: SupportChatViewModel = viewModel()
) {
    // 2. Detectamos la orientación actual del dispositivo
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Soporte", fontWeight = FontWeight.Bold) })
        }
    ) { paddingValues ->
        // 3. Mostramos una UI diferente según la orientación
        if (isLandscape) {
            LandscapeChatLayout(
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel
            )
        } else {
            PortraitChatLayout(
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel
            )
        }
    }
}

// --- UI para modo Vertical (tu código original) ---
@Composable
fun PortraitChatLayout(modifier: Modifier = Modifier, viewModel: SupportChatViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(uiState.messages.size - 1)
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        ChatMessagesList(
            messages = uiState.messages,
            modifier = Modifier.weight(1f)
        )
        MessageInput(onMessageSent = { text ->
            viewModel.onUserMessageSent(text)
        })
    }
}

// --- UI para modo Horizontal (Nuevo diseño de dos paneles) ---
@Composable
fun LandscapeChatLayout(modifier: Modifier = Modifier, viewModel: SupportChatViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Row(modifier = modifier.fillMaxSize()) {
        // Panel izquierdo: Opciones
        QuickOptionsPanel(
            modifier = Modifier.weight(0.4f),
            onOptionClick = { option ->
                viewModel.onUserMessageSent(option)
            }
        )

        // Divisor vertical
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        // Panel derecho: Chat
        ChatMessagesList(
            messages = uiState.messages,
            modifier = Modifier.weight(0.6f)
        )
    }
}

// --- Componentes reutilizables ---

// Panel de opciones rápidas para el modo horizontal
@Composable
fun QuickOptionsPanel(modifier: Modifier = Modifier, onOptionClick: (String) -> Unit) {
    val options = listOf(
        "1" to "¿Cómo subo mis documentos?",
        "2" to "Problemas con un pago",
        "3" to "Mi renta no aparece"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Opciones Rápidas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(options) { (number, text) ->
            OutlinedButton(
                onClick = { onOptionClick(number) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text, textAlign = TextAlign.Center)
            }
        }
    }
}

// Lista de mensajes (extraída para ser reutilizable)
@Composable
fun ChatMessagesList(messages: List<ChatMessage>, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(messages) { message ->
            MessageBubble(message = message)
        }
    }
}

// Burbuja de mensaje (sin cambios)
@Composable
fun MessageBubble(message: ChatMessage) {
    val isFromUser = message.sender == MessageSender.USER
    val bubbleColor = if (isFromUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val alignment = if (isFromUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (isFromUser) 48.dp else 0.dp,
                end = if (isFromUser) 0.dp else 48.dp
            ),
        contentAlignment = alignment
    ) {
        Text(
            text = message.text,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(bubbleColor)
                .padding(12.dp)
        )
    }
}

// Campo de entrada de mensaje (sin cambios)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(onMessageSent: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un número...") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
            IconButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onMessageSent(text)
                        text = ""
                    }
                },
                enabled = text.isNotBlank()
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
            }
        }
    }
}
