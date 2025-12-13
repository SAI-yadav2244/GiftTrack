package uk.ac.tees.mad.gifttrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.gifttrack.navigation.AppNavHost
import uk.ac.tees.mad.gifttrack.navigation.BottomNavBar
import uk.ac.tees.mad.gifttrack.navigation.Routes
import uk.ac.tees.mad.gifttrack.ui.viewmodel.AddGiftViewModel
import uk.ac.tees.mad.gifttrack.ui.viewmodel.CaptureViewModel
import uk.ac.tees.mad.gifttrack.ui.viewmodel.GiftListViewModel
import uk.ac.tees.mad.gifttrack.ui.viewmodel.AuthViewModel
import uk.ac.tees.mad.gifttrack.ui.theme.GiftTrackTheme
import uk.ac.tees.mad.gifttrack.ui.viewmodel.ProfileViewModel


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.DEFAULT_SIGN_IN
        setContent {
            GiftTrackTheme {
//                Surface(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .windowInsetsPadding(WindowInsets.safeDrawing),
//                    color = MaterialTheme.colorScheme.onBackground,
//                ) {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()
                val giftListViewModel: GiftListViewModel = hiltViewModel()
                val addGiftViewModel: AddGiftViewModel = hiltViewModel()
                val captureViewModel: CaptureViewModel = hiltViewModel()
                val profileViewModel: ProfileViewModel = hiltViewModel()
                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                val showBottomBar = currentRoute in listOf(
                    Routes.GIFT_LIST.route,
                    Routes.CALENDAR.route,
                    Routes.PROFILE.route
                )
                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(navController)
                        }
                    },
                    floatingActionButton = {
                        if (currentRoute == "gift_list") {
                            FloatingActionButton(
                                onClick = { navController.navigate(Routes.ADD_GIFT.route) },
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                elevation = FloatingActionButtonDefaults.elevation(
                                    defaultElevation = 8.dp,
                                    pressedElevation = 12.dp
                                )
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Gift")
                            }
                        }
                    },
                ) { innerPadding ->
                    AppNavHost(
                        navController,
                        authViewModel,
                        giftListViewModel,
                        modifier = Modifier.padding(innerPadding),
                        addGiftViewModel = addGiftViewModel,
                        captureViewModel = captureViewModel,
                        profileViewModel = profileViewModel
                    )
                }
//                }
            }
        }
    }
}
