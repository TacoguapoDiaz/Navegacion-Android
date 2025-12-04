package com.example.moverentals2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.moverentals2.R

@Composable
fun PayPalScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) { innerPadding ->
        Image(
            painter = painterResource(id = R.drawable.placeholder_paypal_login),
            contentDescription = stringResource(id = R.string.paypal_login),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentScale = ContentScale.Crop // O usa ContentScale.FillBounds si quieres que se estire
        )
    }
}
