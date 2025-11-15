package com.example.moverentals2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moverentals2.R
import drawable.screens.AppBottomNavigationBar

@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Icono de filtro/ajustes en la esquina superior derecha
            IconButton(
                onClick = { navController.navigate("settings") },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_menu), contentDescription = "Settings")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen de perfil
            Image(
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(150.dp)
                    .border(2.dp, Color.Black, CircleShape)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre de usuario
            Text(
                text = "PEPE",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFBDE0FE), RoundedCornerShape(8.dp))
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Biografía del usuario
            Text(
                text = stringResource(id = R.string.user_bio),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFCDB2), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Calificación con estrellas
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    repeat(5) {
                        Icon(painterResource(id = R.drawable.ic_star), contentDescription = null, Modifier.size(32.dp))
                    }
                }
                Text("5/5", fontWeight = FontWeight.Bold)
            }


            Spacer(modifier = Modifier.weight(1f))

            // Botón de Información Personal
            Button(
                onClick = { navController.navigate("personal_info") },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0096C7))
            ) {
                Text(stringResource(id = R.string.personal_information), color = Color.White)
            }
        }
    }
}
