package uk.ac.tees.mad.gifttrack.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import uk.ac.tees.mad.gifttrack.ui.add_gift.AddGiftScreen
import uk.ac.tees.mad.gifttrack.ui.add_gift.AddGiftViewModel
import uk.ac.tees.mad.gifttrack.ui.screens.auth.AuthScreen
import uk.ac.tees.mad.gifttrack.ui.screens.CalendarScreen
import uk.ac.tees.mad.gifttrack.ui.gift.GiftListScreen
import uk.ac.tees.mad.gifttrack.ui.gift.GiftListViewModel
import uk.ac.tees.mad.gifttrack.ui.screens.ProfileScreen
import uk.ac.tees.mad.gifttrack.ui.screens.SplashScreen
import uk.ac.tees.mad.gifttrack.ui.screens.auth.AuthViewModel

sealed class Routes(val route: String) {
    data object SPLASH : Routes("splash")
    data object AUTH : Routes("auth")
    data object GIFT_LIST : Routes("gift_list")
    data object ADD_GIFT : Routes("add_gift")
    data object CALENDAR : Routes("calendar")
    data object PROFILE : Routes("profile")

    data object ADD_GIFT_WITH_OCCASION : Routes("add_gift/{occasionId}") {
        fun createRoute(occasionId: String) = "add_gift/$occasionId"
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    giftListViewModel: GiftListViewModel,
    addGiftViewModel: AddGiftViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH.route
    ) {
        composable(Routes.SPLASH.route) {
            SplashScreen(
                navigateToAuth = {
                    navController.navigate(Routes.AUTH.route) {
                        popUpTo(Routes.SPLASH.route) { inclusive = true }
                    }
                },
                navigateToHome = {
                    navController.navigate(Routes.GIFT_LIST.route) {
                        popUpTo(Routes.SPLASH.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.AUTH.route) {
            AuthScreen(
                authViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.GIFT_LIST.route) {
                        popUpTo(Routes.AUTH.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.GIFT_LIST.route) {
            GiftListScreen(
                onAddClick = { navController.navigate(Routes.ADD_GIFT.route) },
                onCalendarClick = { navController.navigate(Routes.CALENDAR.route) },
                onProfileClick = { navController.navigate(Routes.PROFILE.route) },
                viewModel = giftListViewModel
            )
        }

        composable(Routes.ADD_GIFT.route) {
            AddGiftScreen(onBack = { navController.navigateUp() }, onSave = {}, addGiftViewModel)
        }

        composable(Routes.CALENDAR.route) {
            CalendarScreen(onBack = { navController.navigateUp() })
        }

        composable(Routes.PROFILE.route) {
            ProfileScreen(
                onLogoutClick = {
                    navController.navigate(Routes.AUTH.route) {
                        popUpTo(Routes.GIFT_LIST.route) { inclusive = true }
                    }
                },
                onBack = { navController.navigateUp() }
            )
        }

    }
}