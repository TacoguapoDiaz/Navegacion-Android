package com.example.moverentals2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moverentals2.R
import com.example.moverentals2.ui.screens.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalHistoryScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(navController, "Historial de rentas", Color(0xFF8ECAE6)) },
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
            item { HistoryItem("Versa", R.drawable.nissan_versa_blue) }
            item { HistoryItem("Title", R.drawable.mazda_5cx) }
        }
    }
}

@Composable
private fun HistoryItem(title: String, imageRes: Int) {
    Column {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Body text for whatever you'd like to say. Add main takeaway points, quotes, anecdotes, or even a very very short story.")
        Row(modifier=Modifier.fillMaxWidth()) {
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8ECAE6)), shape = RoundedCornerShape(50)) { Text("Ver", color = Color.Black) }
            Spacer(modifier=Modifier.weight(1f))
            Image(painterResource(id = imageRes), null, modifier = Modifier.size(150.dp, 100.dp).clip(RoundedCornerShape(16.dp)), contentScale = ContentScale.Crop)
        }
    }
}
