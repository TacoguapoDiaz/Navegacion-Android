package com.example.moverentals2.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
// --- CORRECCIÓN: Se importan las pantallas desde la ubicación correcta ---
import com.example.moverentals2.ui.screens.*
// --- CORRECCIÓN: Se importa el ViewModel ---
import com.example.moverentals2.ui.viewmodels.ThemeViewModel
import drawable.screens.AddReviewScreen
import drawable.screens.CarDetailScreen
import drawable.screens.CarListScreen
import drawable.screens.FavoritesHubScreen
import drawable.screens.FavoritesListScreen
import drawable.screens.FilterScreen
import drawable.screens.LoadingScreen
import drawable.screens.LoginScreen
import drawable.screens.OrderSummaryScreen
import drawable.screens.PayPalScreen
import drawable.screens.PaymentMethodScreen
import drawable.screens.PaymentSuccessScreen
import drawable.screens.RecentlyViewedScreen
import drawable.screens.RegistrationScreen
import drawable.screens.RentalConditionsScreen
import drawable.screens.UploadCarDetailsScreen
import drawable.screens.UploadCarSettingsScreen
import drawable.screens.UploadCarSuccessScreen

@Composable
// --- CORRECCIÓN PRINCIPAL: Se añade el parámetro themeViewModel a la función ---
fun AppNavigation(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "loading"
    ) {
        // --- Flujo Principal ---
        composable("loading") { LoadingScreen(navController = navController) }
        composable("login") { LoginScreen(navController = navController) }
        composable("registration") { RegistrationScreen(navController = navController) }
        composable("car_list") { CarListScreen(navController = navController) }
        composable("filters") { FilterScreen(navController = navController) }
        composable("car_detail") { CarDetailScreen(navController = navController) }

        // --- Flujo de Renta ---
        composable("rental_conditions") { RentalConditionsScreen(navController = navController) }
        composable("add_review") { AddReviewScreen(navController = navController) }
        composable("order_summary") { OrderSummaryScreen(navController = navController) }
        composable("payment_method") { PaymentMethodScreen(navController = navController) }
        composable("paypal") { PayPalScreen(navController = navController) }
        composable("payment_success") { PaymentSuccessScreen(navController = navController) }

        // --- Flujo de Publicación ---
        composable("upload_details") { UploadCarDetailsScreen(navController = navController) }
        composable("upload_settings") { UploadCarSettingsScreen(navController = navController) }
        composable("upload_success") { UploadCarSuccessScreen(navController = navController) }

        // --- Flujo de Favoritos ---
        composable("favorites_hub") { FavoritesHubScreen(navController = navController) }
        composable("recently_viewed") { RecentlyViewedScreen(navController = navController) }
        composable("favorites_list") { FavoritesListScreen(navController = navController) }

        // --- Flujo de Mensajes ---
        composable("message_list") { MessageListScreen(navController = navController) }
        composable(
            route = "chat/{chatType}",
            arguments = listOf(navArgument("chatType") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatType = backStackEntry.arguments?.getString("chatType") ?: "renter"
            ChatScreen(navController = navController, chatType = chatType)
        }

        // --- Flujo de Perfil y Ajustes ---
        composable("profile") { ProfileScreen(navController = navController) }
        composable("personal_info") { PersonalInfoScreen(navController = navController) }
        composable("settings") { SettingsScreen(navController = navController) }
        composable("rental_history") { RentalHistoryScreen(navController = navController) }
        composable("support") { SupportScreen(navController = navController) }
        composable("offers") { OffersScreen(navController = navController) }
        composable("language") { LanguageScreen(navController = navController) }
        composable("currency") { CurrencyScreen(navController = navController) }
        composable("dark_mode") {
            DarkModeScreen(navController = navController, themeViewModel = themeViewModel)
        }
        composable("notifications") { NotificationsScreen(navController = navController) }


// ...
    }
}
