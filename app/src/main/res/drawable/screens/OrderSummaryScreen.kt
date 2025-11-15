package drawable.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moverentals2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.payment_method), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.car_details_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD90429), // Rojo
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        // Esto asegura que el contenido no se solape con las barras del sistema
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Sección Tarjeta de Crédito/Débito ---
            item {
                Text(
                    text = stringResource(R.string.credit_debit_card),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campos de la tarjeta
                PaymentTextField(label = stringResource(R.string.card_number))
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        PaymentTextField(label = stringResource(R.string.card_expiry))
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        PaymentTextField(label = stringResource(R.string.card_cvv))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                PaymentTextField(label = stringResource(R.string.card_holder_name))

                Spacer(modifier = Modifier.height(24.dp))

                // Iconos de tarjetas
                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CardIcon(R.drawable.ic_visa) // Necesitas estos iconos en drawable
                    CardIcon(R.drawable.ic_mastercard)
                    CardIcon(R.drawable.ic_apple_pay)
                    CardIcon(R.drawable.ic_amex)
                }
            }

            // --- Sección Otras Opciones ---
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.other_options),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))


                Button(
                    onClick = { navController.navigate("paypal") },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0070BA)), // Azul PayPal
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_paypal),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.pay_with_paypal), fontSize = 16.sp)
                }
            }

            // --- Botón Final de Pagar ---
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { navController.navigate("payment_success") },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8ECAE6)), // Azul claro
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp)
                ) {
                    Text(stringResource(R.string.pay), color = Color.Black, fontSize = 18.sp)
                }
            }
        }
    }
}

// Composable reutilizable para los campos de texto de pago
@Composable
fun PaymentTextField(label: String) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )
}

// Composable para los iconos de las tarjetas
@Composable
fun CardIcon(iconRes: Int) {
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        modifier = Modifier.height(24.dp)
    )
}
