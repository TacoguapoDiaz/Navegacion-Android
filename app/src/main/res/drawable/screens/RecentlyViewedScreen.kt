package drawable.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moverentals2.R
import com.example.moverentals2.ui.screensimport.CarListItem
import com.example.moverentals2.ui.screensimport.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentlyViewedScreen(navController: NavController) {
    // Simula si hay contenido o no. Cambia a 'true' para ver la lista.
    val hasContent by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.recently_viewed), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.car_details_back)
                        )
                    }
                }
            )
        },
        bottomBar = { AppBottomNavigationBar(navController = navController) },
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) { innerPadding ->
        if (hasContent) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        stringResource(R.string.today),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                item {
                    // Reutilizamos la tarjeta de auto, pero la adaptamos a un estilo de lista
                    CarListItem(
                        imageRes = R.drawable.nissan_versa_grey, // Necesitas esta imagen
                        carName = "Versa 2025",
                        rating = 4.5
                    )
                }
            }
        } else {
            EmptyState(
                modifier = Modifier.padding(innerPadding),
                title = stringResource(R.string.no_cars_viewed),
                subtitle = stringResource(R.string.no_cars_viewed_desc),
                buttonText = stringResource(R.string.start_exploring),
                onButtonClick = { navController.navigate("car_list") }
            )
        }
    }
}
