package drawable.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadCarDetailsScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            // Reutilizamos la barra de navegación inferior
            AppBottomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            Button(
                onClick = { navController.navigate("upload_settings") },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD90429)),
                modifier = Modifier.padding(bottom = 60.dp) // Espacio para que no choque con la nav bar
            ) {
                Text(
                    text = stringResource(R.string.next),
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
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.placeholder_upload_car),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    UploadDropdownField(label = stringResource(R.string.vehicle_type), placeholder = stringResource(R.string.vehicle_type_placeholder))
                    UploadDropdownField(label = stringResource(R.string.year))
                    UploadDropdownField(label = stringResource(R.string.vehicle))
                    UploadDropdownField(label = stringResource(R.string.model))
                    UploadDropdownField(label = stringResource(R.string.number_of_doors))
                }
            }
        }
    }
}

// Composable reutilizable para los campos de texto con ícono de desplegable
@Composable
fun UploadDropdownField(label: String, placeholder: String = "") {
    OutlinedTextField(
        value = placeholder,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        trailingIcon = {
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}
