package uk.ac.tees.mad.gifttrack.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.tees.mad.gifttrack.data.remote.EtsyViewModel
import uk.ac.tees.mad.gifttrack.ui.screens.EditGiftScreen
import uk.ac.tees.mad.gifttrack.ui.screens.AddGiftScreen
import uk.ac.tees.mad.gifttrack.ui.viewmodel.AddGiftViewModel
import uk.ac.tees.mad.gifttrack.ui.screens.CameraScreen
import uk.ac.tees.mad.gifttrack.ui.viewmodel.CaptureViewModel
import uk.ac.tees.mad.gifttrack.ui.screens.GiftListScreen
import uk.ac.tees.mad.gifttrack.ui.viewmodel.GiftListViewModel
import uk.ac.tees.mad.gifttrack.ui.screens.CalendarScreen
import uk.ac.tees.mad.gifttrack.ui.screens.ProfileScreen
import uk.ac.tees.mad.gifttrack.ui.screens.SplashScreen
import uk.ac.tees.mad.gifttrack.ui.screens.AuthScreen
import uk.ac.tees.mad.gifttrack.ui.viewmodel.AuthViewModel
import uk.ac.tees.mad.gifttrack.ui.viewmodel.ProfileViewModel

sealed class Routes(val route: String) {
    data object SPLASH : Routes("splash")
    data object AUTH : Routes("auth")
    data object GIFT_LIST : Routes("gift_list")
    data object ADD_GIFT : Routes("add_gift")
    data object CALENDAR : Routes("calendar")
    data object PROFILE : Routes("profile")
    data object CAMERA : Routes("camera")

    data object EDIT_GIFT : Routes("edit_gift/{giftId}") {
        fun createRoute(giftId: String) = "edit_gift/$giftId"
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    giftListViewModel: GiftListViewModel,
    addGiftViewModel: AddGiftViewModel,
    captureViewModel: CaptureViewModel,
    profileViewModel: ProfileViewModel,
    etsyViewModel: EtsyViewModel,
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
                },
                viewModel = etsyViewModel
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
                viewModel = giftListViewModel,
                onGiftClick = { giftId ->
                    navController.navigate(Routes.EDIT_GIFT.createRoute(giftId))
                }
            )
        }

        composable(Routes.ADD_GIFT.route) {
            AddGiftScreen(
                onBack = { navController.navigateUp() },
                onSave = { navController.navigateUp() },
                onNavigateToCamera = {
                    navController.navigate(
                        Routes.CAMERA.route
                    )
                },
                viewModel = addGiftViewModel,
                etsyViewModel = etsyViewModel
            )
        }

        composable(Routes.EDIT_GIFT.route) { backStackEntry ->
            val giftId = backStackEntry.arguments?.getString("giftId")
            val gift = giftListViewModel.gifts.collectAsState().value.find { it.id == giftId }
            if (gift != null) {
                EditGiftScreen(
                    gift = gift,
                    viewModel = addGiftViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(Routes.CALENDAR.route) {
            CalendarScreen(
                onBack = { navController.navigateUp() }, giftListViewModel = giftListViewModel, onAddGiftForDate = { date ->
                    navController.navigate(Routes.ADD_GIFT.route)
                },
                onEditGiftForDate = { navController.navigate(Routes.EDIT_GIFT.route) })
        }

        composable(Routes.PROFILE.route) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Routes.AUTH.route) {
                        popUpTo(Routes.GIFT_LIST.route) { inclusive = true }
                    }
                },
                viewModel = profileViewModel
            )
        }
        composable(Routes.CAMERA.route) {
            CameraScreen(
                onImageCaptured = { uri ->
                    addGiftViewModel.onImageCaptured(uri)
//                    navController.navigateUp()
//                    val vm = hiltViewModel<AddGiftViewModel>(
//                        navController.getBackStackEntry(Routes.ADD_GIFT.route)
//                    )
//                    vm.onImageCaptured(uri)
                },
                onBack = { navController.navigateUp() },
                viewModel = captureViewModel
            )
        }

    }
}