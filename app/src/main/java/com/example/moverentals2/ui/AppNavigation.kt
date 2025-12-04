package com.example.moverentals2.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.moverentals2.R
import com.example.moverentals2.ui.screens.*
import com.example.moverentals2.ui.viewmodels.AuthViewModel
import com.example.moverentals2.ui.viewmodels.AuthState
import com.example.moverentals2.ui.viewmodels.CarDetailViewModel
import com.example.moverentals2.ui.viewmodels.OrderViewModel
import com.example.moverentals2.ui.screens.*
import com.example.moverentals2.ui.screens.DarkModeScreen //


object CarDetailViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = extras.createSavedStateHandle()
        return CarDetailViewModel(savedStateHandle) as T
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()

    val startDestination = if (authState is AuthState.Authenticated) "main_graph" else "auth_graph"

    NavHost(navController = navController, startDestination = startDestination) {


        navigation(startDestination = "login", route = "auth_graph") {
            composable("login") {
                LoginScreen(
                    authViewModel = authViewModel,
                    onNavigateToMain = {
                        navController.navigate("main_graph") {
                            popUpTo("auth_graph") { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    }
                )
            }
            composable("register") {
                RegistrationScreen(
                    authViewModel = authViewModel,
                    onNavigateToMain = {
                        navController.navigate("main_graph") {
                            popUpTo("auth_graph") { inclusive = true }
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }


        composable(route = "main_graph") {
            MainScreen(
                onNavigateToAuth = {
                    navController.navigate("auth_graph") {
                        popUpTo("main_graph") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun MainScreen(onNavigateToAuth: () -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "car_list",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("car_list") { HomeScreen(navController) }
            composable("favorites_hub") { FavoritesHubScreen(navController) }
            composable("recently_viewed_screen") { RecentlyViewedScreen(navController) }
            composable("filter") { FilterScreen(navController) }
            composable("upload") { UploadCarDetailsScreen(navController) }

            composable("chat") { Box(Modifier.fillMaxSize(), Alignment.Center) { Text("Chat Screen Placeholder") } }
            composable("support_chat") {
                SupportChatScreen(navController = navController)
            }

            composable("profile") {
                ProfileScreen(
                    navController = navController,
                    onNavigateToAuth = onNavigateToAuth
                )
            }
            composable("documents") {
                DocumentsScreen(navController = navController)
            }

            composable("rental_conditions") { RentalConditionsScreen(navController) }
            composable("settings") { SettingsScreen(navController) }
            composable("favorites_screen") { FavoritesScreen(navController) }
            composable("rental_success") { RentalSuccessScreen(navController) }



            composable("language") {
                LanguageScreen(navController = navController)
            }
            composable("currency") {
                CurrencyScreen(navController = navController)
            }

            composable("car_detail/{carId}", arguments = listOf(navArgument("carId") { type = NavType.StringType })) { backStackEntry ->
                val carDetailViewModel: CarDetailViewModel = viewModel(factory = CarDetailViewModelFactory, viewModelStoreOwner = backStackEntry)
                CarDetailScreen(navController = navController, carDetailViewModel = carDetailViewModel)
            }
            composable("reviews/{carId}", arguments = listOf(navArgument("carId") { type = NavType.StringType })) { navBackStackEntry ->
                val parentRoute = "car_detail/${navBackStackEntry.arguments?.getString("carId")}"
                val parentEntry = remember(navBackStackEntry) { navController.getBackStackEntry(parentRoute) }
                val carDetailViewModel: CarDetailViewModel = viewModel(viewModelStoreOwner = parentEntry)
                ReviewListScreen(navController = navController, carDetailViewModel = carDetailViewModel)
            }
            composable("add_review/{carId}", arguments = listOf(navArgument("carId") { type = NavType.StringType })) { navBackStackEntry ->
                val parentRoute = "car_detail/${navBackStackEntry.arguments?.getString("carId")}"
                val parentEntry = remember(navBackStackEntry) { navController.getBackStackEntry(parentRoute) }
                val carDetailViewModel: CarDetailViewModel = viewModel(viewModelStoreOwner = parentEntry)
                AddReviewScreen(navController = navController, carDetailViewModel = carDetailViewModel)
            }
            composable("order_summary/{carId}", arguments = listOf(navArgument("carId") { type = NavType.StringType })) { OrderSummaryScreen(navController) }
            composable("payment_method/{carId}", arguments = listOf(navArgument("carId") { type = NavType.StringType })) { backStackEntry ->
                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("order_summary/{carId}") }
                val orderViewModel: OrderViewModel = viewModel(parentEntry)
                PaymentMethodScreen(navController = navController, orderViewModel = orderViewModel)
            }
        }
    }
}

@Composable
fun AppBottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Upload,
        BottomNavItem.Favorites,
        BottomNavItem.Search,
        BottomNavItem.Chat,
        BottomNavItem.Profile
    )
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Box(
                        modifier = Modifier.then(
                            if (isSelected) Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFE8EAF6)) else Modifier
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(painter = painterResource(id = item.iconRes), contentDescription = item.title, modifier = Modifier.size(24.dp))
                    }
                },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.Gray, indicatorColor = Color.Transparent)
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val title: String, val iconRes: Int) {
    object Upload : BottomNavItem("upload", "Upload", R.drawable.ic_upload)
    object Favorites : BottomNavItem("favorites_hub", "Favorites", R.drawable.ic_heart_filled)
    object Search : BottomNavItem("car_list", "Search", R.drawable.ic_search)
    object Chat : BottomNavItem("support_chat", "Chat", R.drawable.ic_chat)
    object Profile : BottomNavItem("profile", "Profile", R.drawable.ic_person)
}
