package com.example.moverentals2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.moverentals2.R
import com.example.moverentals2.ui.viewmodels.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(
    navController: NavController,
    orderViewModel: OrderViewModel // Compartido
) {
    val uiState by orderViewModel.uiState.collectAsState()

    // --- ESTADOS PARA LOS CAMPOS DEL FORMULARIO ---
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var cardholderName by remember { mutableStateOf("") }

    // --- LÓGICA PARA HABILITAR EL BOTÓN ---
    val isFormComplete = cardNumber.isNotBlank() && expiryDate.isNotBlank() && cvv.isNotBlank() && cardholderName.isNotBlank()

    // Navegar a la pantalla de éxito cuando el alquiler se complete
    LaunchedEffect(uiState.rentalSuccess) {
        if (uiState.rentalSuccess) {
            navController.navigate("rental_success") {
                // Limpiar el backstack para no volver a las pantallas de pago
                popUpTo(navController.graph.findStartDestination().id)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment Method") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Go back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD32F2F),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp).fillMaxSize()) {
            Text("CREDIT / DEBIT CARD", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            // --- FORMULARIO CONECTADO A LOS ESTADOS ---
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = { Text("Card Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    label = { Text("MM/YY") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    label = { Text("CVV") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cardholderName,
                onValueChange = { cardholderName = it },
                label = { Text("Cardholder Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- AQUÍ IRÍAN TUS ICONOS DE TARJETAS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Si tienes tus iconos de visa, mastercard, etc. en drawable, ponlos aquí.
                // Esta es una imagen de ejemplo.
                Image(
                    painter = painterResource(id = R.drawable.ic_visa),
                    contentDescription = "Card Types",
                    modifier = Modifier.height(60.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_mastercard),
                    contentDescription = "Card Types",
                    modifier = Modifier.height(60.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_apple_pay),
                    contentDescription = "Card Types",
                    modifier = Modifier.height(60.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_amex),
                    contentDescription = "Card Types",
                    modifier = Modifier.height(60.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón Pagar final
            Button(
                onClick = { orderViewModel.processRental() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                // --- BOTÓN HABILITADO SEGÚN LA LÓGICA ---
                enabled = !uiState.isProcessingPayment && isFormComplete,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF87CEEB)) // Azul cielo
            ) {
                if (uiState.isProcessingPayment) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Pay", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}
