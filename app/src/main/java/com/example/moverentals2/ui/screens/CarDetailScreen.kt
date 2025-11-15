package drawable.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moverentals2.R

@Composable
fun CarDetailScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // CORRECCIÓN: Se añadió .windowInsetsPadding(WindowInsets.safeDrawing) a la LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // --- Imagen superior del carro y botón de regreso ---
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.nissan_versa),
                        contentDescription = "Nissan Versa",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.White.copy(alpha = 0.8f), CircleShape) // Fondo semi-transparente
                            .align(Alignment.TopStart) // Asegura que esté en la esquina superior izquierda
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = stringResource(R.string.car_details_back), tint = Color.Black)
                    }
                }
            }

            // --- Título del carro ---
            item {
                Text(
                    text = "Nissan Versa 2020",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )
            }

            // --- Lista de detalles (4 Puertas, Transmisión, etc.) ---
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DetailItem(iconRes = R.drawable.ic_door, text = stringResource(R.string.four_doors))
                    DetailItem(iconRes = R.drawable.ic_car, text = stringResource(R.string.auto_transmission))
                    // Estrellas de calificación, no es un DetailItem normal
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.ic_star), contentDescription = null, modifier = Modifier.size(28.dp), tint = Color(0xFFFFC107)) // Estrella amarilla
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("4.5", fontSize = 18.sp)
                    }
                    DetailItem(iconRes = R.drawable.ic_phone, text = stringResource(R.string.contact_owner))
                }
            }

            // --- Sección de Pick Up y Mapa ---
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.ic_location), contentDescription = null, modifier = Modifier.size(28.dp)) // Icono de ubicación un poco más grande
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.pick_up), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.placeholder_map),
                        contentDescription = stringResource(R.string.map),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // --- Botón de Condiciones de Renta ---
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Button(
                        onClick = { navController.navigate("rental_conditions") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(stringResource(R.string.rental_conditions), color = Color.Black, fontSize = 16.sp)
                    }
                }
            }

            // --- Sección de Reseña (Review) ---
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    ReviewItem(navController = navController)
                    Spacer(modifier = Modifier.height(100.dp)) // Espacio para que no choque con la barra inferior
                }
            }
        }

        // --- Barra inferior fija de Renta ---
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(stringResource(R.string.price_format, 650), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(stringResource(R.string.per_day), fontSize = 14.sp, color = Color.Gray)
                }
                Button(
                    onClick = { navController.navigate("order_summary") },
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD90429))
                ) {
                    Text(stringResource(R.string.rent), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// --- Composable para un item de la lista de detalles (icono + texto) ---
@Composable
fun DetailItem(iconRes: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 18.sp)
    }
}

// --- Composable para una tarjeta de reseña ---
@Composable
fun ReviewItem(navController: NavController) { // <-- Aceptar NavController
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Row { // Estrellas de reseña (amarillas)
                    repeat(4) {
                        Icon(painter = painterResource(id = R.drawable.ic_star), contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(24.dp))
                    }
                    Icon(painter = painterResource(id = R.drawable.ic_star), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF8ECAE6), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { navController.navigate("add_review") }) { // <-- NAVEGACIÓN
                        Icon(painter = painterResource(id = R.drawable.ic_more_dots), contentDescription = "Opciones", tint = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.review_title), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(stringResource(R.string.review_body), color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder_profile),
                    contentDescription = "Reviewer",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(stringResource(R.string.reviewer_name), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(stringResource(R.string.date), color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}