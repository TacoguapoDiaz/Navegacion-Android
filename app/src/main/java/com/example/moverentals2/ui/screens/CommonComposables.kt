package com.example.moverentals2.ui.screens

import androidx.compose.foundation.Image

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moverentals2.R

/**
 * Composable para mostrar cuando una lista está vacía.
 */
@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = subtitle,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onButtonClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0096C7)) // Azul
        ) {
            Text(buttonText, modifier = Modifier.padding(horizontal = 24.dp))
        }
    }
}

/**
 * Tarjeta de auto simplificada para listas de favoritos o vistos recientemente.
 */
@Composable
fun CarListItem(
    imageRes: Int,
    carName: String,
    rating: Double,
    isFavorite: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = carName,
            modifier = Modifier
                .size(width = 140.dp, height = 90.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(carName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = "Rating",
                    modifier = Modifier.size(16.dp)
                )
                Text(" $rating", fontSize = 14.sp)
            }
        }
        if (isFavorite) {
            Icon(
                painter = painterResource(id = R.drawable.ic_heart_filled),
                contentDescription = "Favorite",
                tint = Color.Red,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { /* TODO: Lógica para quitar de favoritos */ }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, title: String, containerColor: Color = Color.Transparent) {
    TopAppBar(        title = { Text(title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = stringResource(id = R.string.car_details_back)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            // Estos colores se adaptarán automáticamente al modo oscuro/claro
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
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
    val textColor = if (backgroundColor == Color.White) Color.Black else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("car_detail") },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
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

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_heart_outline),
                    contentDescription = stringResource(R.string.nav_favorites),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { /* TODO */ },
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
        NavigationBarItem(selected = false, onClick = { /* TODO: navega a subir */ }, icon = { Icon(painter = painterResource(id = R.drawable.ic_upload), contentDescription = stringResource(R.string.nav_upload), modifier = Modifier.size(iconSize)) })
        NavigationBarItem(selected = false, onClick = { /* TODO: favoritos */ }, icon = { Icon(painter = painterResource(id = R.drawable.ic_heart_filled), contentDescription = stringResource(R.string.nav_favorites), modifier = Modifier.size(iconSize)) })
        NavigationBarItem(selected = false, onClick = { /* TODO: buscar */ }, icon = { Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription = stringResource(R.string.nav_search), modifier = Modifier.size(iconSize)) })
        NavigationBarItem(selected = false, onClick = { /* TODO: chat */ }, icon = { Icon(painter = painterResource(id = R.drawable.ic_chat), contentDescription = stringResource(R.string.nav_chat), modifier = Modifier.size(iconSize)) })
        NavigationBarItem(selected = false, onClick = { /* TODO: perfil */ }, icon = { Icon(painter = painterResource(id = R.drawable.ic_person), contentDescription = stringResource(R.string.nav_profile), modifier = Modifier.size(iconSize)) })
    }
}
