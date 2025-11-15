package com.example.moverentals2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moverentals2.R
import com.example.moverentals2.ui.screensimport.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(navController: NavController) {
    Scaffold(topBar = { TopBar(navController, stringResource(id = R.string.language)) }) { innerPadding ->
        val languages = listOf(
            R.string.spanish,
            R.string.english,
            R.string.french,
            R.string.russian,
            R.string.japanese,
            R.string.chinese
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(languages.size) { index ->
                LanguageButton(text = stringResource(id = languages[index]))
            }
        }
    }
}

@Composable
private fun LanguageButton(text: String) {
    Button(
        onClick = { /* Sin funcionalidad por ahora */ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text)
    }
}
