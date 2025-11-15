package drawable.screens

import androidx.compose.foundation.layout.*
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
import com.example.moverentals2.ui.theme.AppLightBlueRegister
import com.example.moverentals2.ui.theme.AppRedCancel
import com.example.moverentals2.ui.theme.AppWhite

@Composable
fun RegistrationScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back button arriba a la izquierda para consistencia con otras pantallas
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = stringResource(R.string.car_details_back))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(50),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = AppWhite)
        ) {
            Text(
                text = stringResource(R.string.registration),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(
                    horizontal = 64.dp,
                    vertical = 16.dp
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        val fieldModifier = Modifier.fillMaxWidth()
        val fieldShape = RoundedCornerShape(50)
        OutlinedTextField(value = "", onValueChange = {}, label = { Text(stringResource(R.string.first_name)) }, modifier = fieldModifier, shape = fieldShape)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text(stringResource(R.string.last_name)) }, modifier = fieldModifier, shape = fieldShape)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text(stringResource(R.string.user_name)) }, modifier = fieldModifier, shape = fieldShape)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text(stringResource(R.string.email)) }, modifier = fieldModifier, shape = fieldShape)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text(stringResource(R.string.password)) }, modifier = fieldModifier, shape = fieldShape)

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val buttonModifier = Modifier
                .width(140.dp)
                .height(50.dp)
            Button(
                onClick = { /* TODO */ },
                modifier = buttonModifier,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = AppLightBlueRegister)
            ) {
                Text(stringResource(R.string.register), color = Color.White)
            }
            Button(
                onClick = { navController.popBackStack() },
                modifier = buttonModifier,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = AppRedCancel)
            ) {
                Text(stringResource(R.string.cancel), color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
