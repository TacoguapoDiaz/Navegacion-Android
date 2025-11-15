package com.example.moverentals2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moverentals2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, chatType: String) {
    val isAssistanceChat = chatType == "assistance"
    val topBarColor = if (isAssistanceChat) Color(0xFFFFCDB2) else Color(0xFFBDE0FE)
    val contactName = if (isAssistanceChat) stringResource(R.string.customer_assistance) else "Carla Diaz Rentera"
    val iconRes = if (isAssistanceChat) R.drawable.ic_headset else R.drawable.ic_person

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ChatTopBar(
                navController = navController,
                color = topBarColor,
                contactName = contactName,
                iconRes = iconRes
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Aquí irían los mensajes. Por ahora son datos de ejemplo.
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item { MessageBubble("Lo mas probable es que lo ocupe el dia 6 de noviembre y vamos a ser 4 personas", false) }
                item { MessageBubble("Para que dia ocuparia el carro?", true) }
                item { MessageBubble("Asi es, esta disponible", true) }
                item { MessageBubble("Buenas tardes, esta disponible?", false) }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            MessageInputField()
        }
    }
}

@Composable
private fun ChatTopBar(navController: NavController, color: Color, contactName: String, iconRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(color)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Icon(painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(32.dp))
            Text(contactName, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(48.dp)) // Espacio para centrar el texto
    }
}

@Composable
private fun MessageBubble(text: String, isFromMe: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromMe) Arrangement.End else Arrangement.Start
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(
                    color = if (isFromMe) Color.DarkGray else Color.LightGray,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp),
            color = if (isFromMe) Color.White else Color.Black
        )
    }
}

@Composable
private fun MessageInputField() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.weight(1f),
            placeholder = { Text(stringResource(R.string.type_message)) },
            shape = RoundedCornerShape(50)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = { /*TODO: Enviar mensaje*/ },
            modifier = Modifier
                .background(Color.Gray, RoundedCornerShape(50))
        ) {
            Icon(painterResource(id = R.drawable.ic_send), contentDescription = "Send", tint = Color.White)
        }
    }
}
