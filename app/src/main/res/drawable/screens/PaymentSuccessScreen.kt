package drawable.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button


import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moverentals2.R

@Composable
fun PaymentSuccessScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Contenido Central
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.payment_authorized),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_check_circle),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.Green // Tinte verde para el icono de check
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.rental_done),
            fontSize = 24.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_sentiment_satisfied),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.Black
        )

        Spacer(modifier = Modifier.weight(1.5f))

        // Botón "Mis Rentas"
        Button(
            onClick = {
                // TODO: Navegar a la futura pantalla "My Rentals"
                // Por ahora, podemos volver a la lista de autos para no quedarnos atrapados
                navController.navigate("car_list") {
                    // Limpia toda la pila de navegación hasta el inicio para no volver atrás
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8ECAE6)) // Azul claro
        ) {
            Text(
                text = stringResource(id = R.string.my_rentals),
                color = Color.Black,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
