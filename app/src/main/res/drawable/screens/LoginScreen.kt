package drawable.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moverentals2.R
import com.example.moverentals2.ui.theme.AppBlueGradientStart
import com.example.moverentals2.ui.theme.AppRedGradientEnd
import com.example.moverentals2.ui.theme.AppWhite

@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // botón cerrar alineado a la derecha
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .size(40.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cancel), tint = Color.Gray, modifier = Modifier.size(28.dp))
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Card(
            shape = RoundedCornerShape(50),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = AppWhite),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 64.dp, vertical = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.password)) },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_lock), contentDescription = null, modifier = Modifier.size(20.dp))
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate("car_list") {
                    popUpTo("login") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(50)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.horizontalGradient(colors = listOf(AppBlueGradientStart, AppRedGradientEnd))
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.log_in), fontSize = 18.sp, color = AppWhite)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(R.string.or), color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_google), contentDescription = "Google", modifier = Modifier.size(40.dp))
            Icon(painter = painterResource(id = R.drawable.ic_facebook), contentDescription = "Facebook", modifier = Modifier.size(40.dp))
            Icon(painter = painterResource(id = R.drawable.ic_apple), contentDescription = "Apple", modifier = Modifier.size(40.dp))
            Icon(painter = painterResource(id = R.drawable.ic_linkedin), contentDescription = "LinkedIn", modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.dont_have_account))
            TextButton(onClick = { navController.navigate("registration") }) {
                Text(stringResource(R.string.sign_up_button), fontWeight = FontWeight.Bold)
            }
        }
    }
}
