package com.example.moverentals2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moverentals2.R
import drawable.screens.AppBottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.settings_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painterResource(id = R.drawable.ic_arrow_back), null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF8ECAE6), titleContentColor = Color.Black, navigationIconContentColor = Color.Black)
            )
        },
        bottomBar = { AppBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { SettingsItem(text = stringResource(id = R.string.support), icon = R.drawable.ic_headset, onClick = { navController.navigate("support") }) }
            item { SettingsItem(text = stringResource(id = R.string.rental_history), icon = R.drawable.ic_history, onClick = { navController.navigate("rental_history") }) }
            item { SettingsItem(text = stringResource(id = R.string.offers), icon = R.drawable.ic_suitcase, onClick = { navController.navigate("offers") }) }
            item { SettingsItem(text = stringResource(id = R.string.dark_mode), icon = R.drawable.ic_moon, onClick = { navController.navigate("dark_mode") }) }
            item { SettingsItem(text = stringResource(id = R.string.notifications), icon = R.drawable.ic_notifications, onClick = {navController.navigate("notifications") }) }
            item { SettingsItem(text = stringResource(id = R.string.language), icon = R.drawable.ic_language, onClick = { navController.navigate("language") }) }
            item { SettingsItem(text = stringResource(id = R.string.currency), icon = R.drawable.ic_attach_money, onClick = { navController.navigate("currency") }) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsItem(text: String, icon: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(id = icon), contentDescription = null, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
