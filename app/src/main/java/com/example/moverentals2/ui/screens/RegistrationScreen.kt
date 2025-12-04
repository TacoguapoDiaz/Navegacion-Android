package com.example.moverentals2.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
// Ya no necesitamos NavController aquí
import com.example.moverentals2.R
import com.example.moverentals2.ui.viewmodels.AuthState
import com.example.moverentals2.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onNavigateToMain: () -> Unit, // Lambda para ir al inicio tras registro exitoso
    onNavigateBack: () -> Unit,   // Lambda para volver atrás
    authViewModel: AuthViewModel
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                // CORRECCIÓN: Usamos la lambda para ir al grafo principal
                onNavigateToMain()
            }
            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                authViewModel.resetAuthState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    // CORRECCIÓN: Usamos la lambda para volver
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { /* No-op */ },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(text = stringResource(id = R.string.registration), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text(stringResource(id = R.string.first_name)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text(stringResource(id = R.string.last_name)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = userName, onValueChange = { userName = it }, label = { Text(stringResource(id = R.string.user_name)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text(stringResource(id = R.string.email)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text(stringResource(id = R.string.password)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50), visualTransformation = PasswordVisualTransformation())
            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (firstName.isNotBlank() && lastName.isNotBlank() && userName.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                            authViewModel.signUp(
                                email = email.trim(),
                                password = password,
                                firstName = firstName.trim(),
                                lastName = lastName.trim(),
                                userName = userName.trim()
                            )
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6)),
                    enabled = authState != AuthState.Authenticating
                ) {
                    if (authState == AuthState.Authenticating) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text(text = stringResource(id = R.string.register), color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { onNavigateBack() }, // Usamos la lambda
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF08080))
                ) {
                    Text(text = stringResource(id = R.string.cancel), color = Color.White)
                }
            }
        }
    }
}