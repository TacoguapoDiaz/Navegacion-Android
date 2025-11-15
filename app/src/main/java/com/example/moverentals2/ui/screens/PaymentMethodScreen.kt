package drawable.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moverentals2.R
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(navController: NavController) {
    // Estados para controlar la visibilidad del Date Picker y los detalles
    var showDatePicker by remember { mutableStateOf(false) }
    var showDetails by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.order_summary), fontWeight = FontWeight.Bold) },
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
        // Aplicamos el padding de WindowInsets aquí
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF8ECAE6)) // Fondo azul claro
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.nissan_versa),
                contentDescription = "Nissan Versa",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SummaryButton(text = stringResource(id = R.string.selected_days), isBig = true, onClick = {})
                // El botón de Fecha ahora controla el estado del Date Picker
                SummaryButton(text = stringResource(id = R.string.date_label), isBig = false, onClick = { showDatePicker = true })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SummaryButton(text = stringResource(id = R.string.total_price), isBig = true, onClick = {})
                // El botón de Detalles ahora controla su propia visibilidad
                SummaryButton(text = stringResource(id = R.string.details), isBig = false, onClick = { showDetails = !showDetails })
            }

            // --- Sección de Detalles (Aparece y desaparece) ---
            AnimatedVisibility(visible = showDetails) {
                Card(
                    modifier = Modifier.padding(top = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(stringResource(R.string.details), fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.rental_days))
                        Text(stringResource(R.string.price_with_value, 0.00))
                    }
                }
            }


            Spacer(modifier = Modifier.weight(1f)) // Espacio flexible

            Text("$0.00mx", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("payment_method") },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A80))
            ) {
                Text(stringResource(id = R.string.pay), fontSize = 18.sp, color = Color.Black)
            }
        }
    }

    // --- Date Picker Dialog ---
    // Se mostrará como un diálogo sobre la pantalla actual cuando showDatePicker sea true
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    // Aquí podrías usar la fecha seleccionada: datePickerState.selectedDateMillis
                }) {
                    Text(stringResource(R.string.date_picker_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(stringResource(R.string.date_picker_title), modifier = Modifier.padding(16.dp))
                }
            )
        }
    }
}

// Este Composable ya lo tenías, pero es bueno tenerlo en el mismo archivo para que esté completo
@Composable
fun RowScope.SummaryButton(text: String, isBig: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .weight(if (isBig) 0.6f else 0.4f)
            .height(60.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isBig) Color.White.copy(alpha = 0.8f) else Color(0xFFFF8A80)
        )
    ) {
        Text(text, color = Color.Black, fontSize = if (isBig) 16.sp else 14.sp)
    }
}
