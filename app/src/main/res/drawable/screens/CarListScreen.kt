package drawable.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListScreen(navController: NavController) {
    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Padding del Scaffold
            contentPadding = PaddingValues(horizontal = 16.dp), // Padding horizontal para todo
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Espaciador para la barra de estado ---
            item {
                Spacer(modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(top = 16.dp)) // <-- CORRECCIÓN: Espacio superior
            }

            // --- Barra de Búsqueda de Ubicación ---
            item {
                OutlinedTextField(
                    value = "Location...", // <-- Usa texto directo o un stringResource
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO: Navegar a pantalla de Ubicación */ },
                    shape = RoundedCornerShape(50),
                    trailingIcon = {
                        IconButton(onClick = { /* TODO: mostrar opciones de ubicación */ }) {
                            Icon(painter = painterResource(id = R.drawable.ic_more_dots), contentDescription = stringResource(R.string.filter))
                        }
                    },
                    // <-- CORRECCIÓN: El icono de filtro se quita de aquí
                )
            }

            // --- Botón de Filtros (azul con icono y texto) ---
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { navController.navigate("filters") },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0096C7)),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filters),
                            contentDescription = stringResource(R.string.filter),
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.filter), color = Color.White)
                    }
                }
            }

            // --- Tarjeta de Coche: Nissan Versa ---
            item {
                CarCard(
                    navController = navController,
                    imageRes = R.drawable.nissan_versa, // Asegúrate que esta imagen sea la azul
                    carName = "Versa 2020",
                    rating = 4.5,
                    price = 800,
                    city = "Aguascalientes",
                    owner = "Peter",
                    backgroundColor = Color(0xFF0096C7) // Azul
                )
            }

            // --- Tarjeta de Coche: Mazda 5cx ---
            item {
                CarCard(
                    navController = navController,
                    imageRes = R.drawable.mazda_5cx, // Asegúrate que esta imagen sea la roja
                    carName = "Mazda 5cx",
                    rating = 3.8,
                    price = 650,
                    city = "Aguascalientes",
                    owner = "Macario",
                    backgroundColor = Color.White // Blanco
                )
            }

            // Espacio final para que la última tarjeta no pegue con la barra de navegación
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CarCard(
    navController: NavController,
    imageRes: Int,
    carName: String,
    rating: Double,
    price: Int,
    city: String,
    owner: String,
    backgroundColor: Color
) {
    // CORRECCIÓN: El color del texto es negro si el fondo es blanco, si no, es blanco.
    val textColor = if (backgroundColor == Color.White) Color.Black else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("car_detail") },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        // CORRECCIÓN: Agregamos elevación a la tarjeta blanca para que se vea
        elevation = if (backgroundColor == Color.White) CardDefaults.cardElevation(4.dp) else CardDefaults.cardElevation(0.dp)
    ) {
        // <-- CORRECCIÓN: Estructura de layout cambiada a Row principal
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Columna Izquierda: Imagen, Ciudad, Dueño
            Column(
                modifier = Modifier.weight(1f) // Ocupa el espacio disponible
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = carName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.car_card_city, city), color = textColor, fontSize = 14.sp)
                Text(stringResource(R.string.car_card_owner, owner), color = textColor, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Columna Derecha: Corazón, Nombre, Rating, Precio
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.width(IntrinsicSize.Max) // Se ajusta al contenido
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_heart_outline),
                    contentDescription = stringResource(R.string.nav_favorites),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { /* TODO: Lógica para favoritos */ },
                    tint = textColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(carName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.ic_star), contentDescription = "Rating", tint = textColor, modifier = Modifier.size(18.dp))
                    Text(text = " ${rating}", color = textColor, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.price_per_day), color = textColor, fontSize = 12.sp)
                Text(stringResource(R.string.price_format, price), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
            }
        }
    }
}

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        val iconSize = 24.dp
        NavigationBarItem(selected = false, onClick = { navController.navigate("upload_details") }, icon = { Icon(painter = painterResource(id = R.drawable.ic_upload), contentDescription = stringResource(R.string.nav_upload), modifier = Modifier.size(iconSize)) })
        NavigationBarItem(selected = false, onClick = { navController.navigate("favorites_hub")}, icon = { Icon(painter = painterResource(id = R.drawable.ic_heart_filled), contentDescription = stringResource(R.string.nav_favorites), modifier = Modifier.size(iconSize)) })
        NavigationBarItem(selected = false, onClick = { navController.navigate("car_list") }, icon = { Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription = stringResource(R.string.nav_search), modifier = Modifier.size(iconSize)) })
        NavigationBarItem(selected = false, onClick = { /*TODO*/ }, icon = { Icon(painter = painterResource(id = R.drawable.ic_chat), contentDescription = stringResource(R.string.nav_chat), modifier = Modifier.size(iconSize)) })
        NavigationBarItem(selected = false, onClick = { /*TODO*/ }, icon = { Icon(painter = painterResource(id = R.drawable.ic_person), contentDescription = stringResource(R.string.nav_profile), modifier = Modifier.size(iconSize)) })
    }
}