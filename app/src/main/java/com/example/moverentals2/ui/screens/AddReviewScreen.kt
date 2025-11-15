package drawable.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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

@androidx.compose.runtime.Composable
fun AddReviewScreen(navController: androidx.navigation.NavController) {
    // CORRECCIÓN: Se añadió .windowInsetsPadding(WindowInsets.safeDrawing)
    _root_ide_package_.androidx.compose.foundation.layout.Column(
        modifier = _root_ide_package_.androidx.compose.ui.Modifier.Companion
            .fillMaxSize()
            .windowInsetsPadding(_root_ide_package_.androidx.compose.foundation.layout.WindowInsets.Companion.safeDrawing)
    ) {
        // Barra superior azul
        _root_ide_package_.androidx.compose.material3.Surface(
            color = _root_ide_package_.com.example.moverentals2.ui.theme.AppLightBlueRegister, // Azul claro
            contentColor = _root_ide_package_.androidx.compose.ui.graphics.Color.Companion.White
        ) {
            _root_ide_package_.androidx.compose.foundation.layout.Row(
                modifier = _root_ide_package_.androidx.compose.ui.Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = _root_ide_package_.androidx.compose.ui.Alignment.Companion.CenterVertically
            ) {
                _root_ide_package_.androidx.compose.material3.IconButton(onClick = { navController.popBackStack() }) {
                    _root_ide_package_.androidx.compose.material3.Icon(
                        painter = _root_ide_package_.androidx.compose.ui.res.painterResource(id = _root_ide_package_.com.example.moverentals2.R.drawable.ic_arrow_back),
                        contentDescription = _root_ide_package_.androidx.compose.ui.res.stringResource(
                            _root_ide_package_.com.example.moverentals2.R.string.car_details_back
                        ),
                        tint = _root_ide_package_.androidx.compose.ui.graphics.Color.Companion.White
                    )
                }
                _root_ide_package_.androidx.compose.foundation.layout.Spacer(
                    modifier = _root_ide_package_.androidx.compose.ui.Modifier.Companion.width(
                        8.dp
                    )
                )
                _root_ide_package_.androidx.compose.material3.Text(
                    text = _root_ide_package_.androidx.compose.ui.res.stringResource(
                        _root_ide_package_.com.example.moverentals2.R.string.add_review
                    ),
                    fontSize = 20.sp,
                    fontWeight = _root_ide_package_.androidx.compose.ui.text.font.FontWeight.Companion.Bold
                )
            }
        }

        // Contenido de la pantalla
        _root_ide_package_.androidx.compose.foundation.layout.Column(
            modifier = _root_ide_package_.androidx.compose.ui.Modifier.Companion
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Fila del usuario
            OutlinedTextField(
                value = stringResource(id = R.string.reviewer_placeholder),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp) // Tamaño ajustado del icono
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Fila de las estrellas
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(5) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp), // Tamaño de las estrellas
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campo de texto para la reseña
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Ocupa el espacio disponible
                placeholder = { Text(text = stringResource(R.string.write_review_placeholder)) },
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val buttonModifier = Modifier.width(140.dp).height(50.dp)
                Button(
                    onClick = { /* TODO: Lógica de publicar */ },
                    modifier = buttonModifier,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = AppLightBlueRegister)
                ) {
                    Text(stringResource(R.string.publish), color = Color.White)
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
        }
    }
}