package com.example.moverentals2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moverentals2.R
import drawable.screens.AppBottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageListScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Rentero") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.messages), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("car_list") { popUpTo("car_list"){ inclusive = true } } }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.car_details_back)
                        )
                    }
                }
            )
        },
        bottomBar = { AppBottomNavigationBar(navController = navController) },
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            MessageTabs(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            // Contenido dinámico según la pestaña seleccionada
            when (selectedTab) {
                "Rentero" -> ConversationList(
                    navController = navController,
                    conversations = listOf(
                        Conversation("Carla Diaz-Rentera", "Último mensaje: hace 2 dias", "renter")
                    )
                )
                "Asistencia" -> ConversationList(
                    navController = navController,
                    conversations = listOf(
                        Conversation("Asistencia al cliente", "Último mensaje: Ayer", "assistance")
                    )
                )
                "Todos" -> EmptyChatState() // Por ahora, la pestaña "Todos" está vacía
            }
        }
    }
}

@Composable
private fun MessageTabs(selectedTab: String, onTabSelected: (String) -> Unit) {
    val tabs = listOf("Todos", "Rentero", "Asistencia")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tabs.forEach { tab ->
            val isSelected = selectedTab == tab
            Button(
                onClick = { onTabSelected(tab) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) Color.Black else Color.LightGray,
                    contentColor = if (isSelected) Color.White else Color.Black
                )
            ) {
                Text(tab)
            }
        }
    }
}

@Composable
private fun ConversationList(navController: NavController, conversations: List<Conversation>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(conversations.size) { index ->
            val conversation = conversations[index]
            ConversationItem(
                conversation = conversation,
                onClick = {
                    navController.navigate("chat/${conversation.type}")
                }
            )
        }
    }
}

data class Conversation(val name: String, val lastMessage: String, val type: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConversationItem(conversation: Conversation, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconRes = if (conversation.type == "renter") R.drawable.ic_person else R.drawable.ic_headset
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(conversation.name, fontWeight = FontWeight.Bold)
                Text(conversation.lastMessage, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun EmptyChatState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chat_bubble_outline),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.no_conversations), fontWeight = FontWeight.Bold)
        Text(
            stringResource(R.string.no_conversations_desc),
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}
