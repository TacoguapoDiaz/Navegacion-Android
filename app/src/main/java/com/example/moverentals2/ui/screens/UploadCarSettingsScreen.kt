package com.example.moverentals2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moverentals2.R
// --- IMPORTACIONES AÑADIDAS ---
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadCarSettingsScreen(navController: NavController) {
    var switchState by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableStateOf(50f) }

    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController = navController) },
        floatingActionButton = {
            Button(
                onClick = { navController.navigate("upload_success") },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD90429)),
                modifier = Modifier.padding(bottom = 60.dp)
            ) {
                Text(
                    text = stringResource(R.string.publish),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Sección Recibir Ofertas
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.receive_offers), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(stringResource(R.string.receive_offers_desc), fontSize = 14.sp, color = Color.Gray)
                    }
                    Switch(checked = switchState, onCheckedChange = { switchState = it })
                }
            }

            // Sección Rango de Precio
            item {
                Text(stringResource(R.string.price_range), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 0f..100f
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("$0")
                    Text("$100")
                }
                Text(stringResource(R.string.price_range_desc), fontSize = 14.sp, color = Color.Gray)
            }

            // Sección Fecha
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(stringResource(R.string.rental_start_date))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = "mm/dd/yyyy",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(id = R.string.date)) },
                            trailingIcon = {
                                // --- CORRECCIÓN AQUÍ ---
                                // Se añade un tamaño fijo al icono del calendario
                                Icon(
                                    painterResource(id = R.drawable.ic_calendar),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Row(modifier = Modifier.align(Alignment.End)) {
                            TextButton(onClick = { /*TODO*/ }) { Text(stringResource(R.string.cancel)) }
                            TextButton(onClick = { /*TODO*/ }) { Text(stringResource(R.string.date_picker_ok)) }
                        }
                    }
                }
            }

            // Sección Descripción
            item {
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    label = { Text(stringResource(R.string.description)) },
                    placeholder = { Text(stringResource(R.string.description_placeholder)) },
                    trailingIcon = { Icon(Icons.Default.Close, contentDescription = null) }
                )
            }

            // Sección Ubicación
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                painterResource(id = R.drawable.ic_more_dots), // Deberías tener un icono de menú
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("UBICACION", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                painterResource(id = R.drawable.ic_location),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(
                                painterResource(id = R.drawable.ic_search),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.placeholder_map),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) } // Espacio para el botón flotante
        }
    }
}
