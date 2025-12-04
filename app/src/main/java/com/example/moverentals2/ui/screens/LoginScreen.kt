package com.example.moverentals2.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moverentals2.R
import com.example.moverentals2.ui.viewmodels.AuthState
import com.example.moverentals2.ui.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth // Importante para forzar el logout

@Composable
fun LoginScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToRegister: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ESTA VARIABLE ES LA CLAVE PARA ROMPER EL BUCLE
    // Solo permitiremos la redirección si el usuario realmente pulsó el botón.
    var userIntendedToLogin by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Authenticated -> {
                if (userIntendedToLogin) {
                    // CASO 1: El usuario escribió su clave y dio click. Todo bien.
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    onNavigateToMain()
                } else {
                    // CASO 2 (TU ERROR): Entraste aquí pero ya estabas "logueado" por error.
                    // En lugar de redirigirte, forzamos el cierre de sesión para que veas el formulario.
                    FirebaseAuth.getInstance().signOut()
                    authViewModel.resetAuthState() // Si tu ViewModel tiene este método
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                authViewModel.resetAuthState()
                userIntendedToLogin = false // Reseteamos el intento
            }
            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        Scaffold(
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(id = R.string.dont_have_account), color = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = { onNavigateToRegister() }) {
                        Text("Sign Up", fontWeight = FontWeight.Bold, color = Color(0xFF3B82F6))
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                Button(
                    onClick = { onNavigateToRegister() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(text = stringResource(id = R.string.sign_in), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(40.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(id = R.string.email)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(id = R.string.password)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon") }
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            // MARCAMOS QUE AHORA SÍ QUEREMOS ENTRAR
                            userIntendedToLogin = true
                            authViewModel.signIn(email, password)
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(50)),
                    enabled = authState != AuthState.Authenticating,
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF3B82F6), Color(0xFFE17055))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (authState == AuthState.Authenticating) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        } else {
                            Text(text = "LOG IN", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = stringResource(id = R.string.or), color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_google), contentDescription = "Google", modifier = Modifier.size(30.dp))
                    Icon(painter = painterResource(id = R.drawable.ic_facebook), contentDescription = "Facebook", modifier = Modifier.size(30.dp))
                    Icon(painter = painterResource(id = R.drawable.ic_apple), contentDescription = "Apple", modifier = Modifier.size(30.dp))
                    Icon(painter = painterResource(id = R.drawable.ic_linkedin), contentDescription = "LinkedIn", modifier = Modifier.size(30.dp))
                }
            }
        }

        IconButton(
            onClick = { onNavigateToMain() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close and go to Home"
            )
        }
    }
}