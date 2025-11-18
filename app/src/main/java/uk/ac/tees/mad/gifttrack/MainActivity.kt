package uk.ac.tees.mad.gifttrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.gifttrack.navigation.AppNavHost
import uk.ac.tees.mad.gifttrack.navigation.BottomNavBar
import uk.ac.tees.mad.gifttrack.navigation.Routes
import uk.ac.tees.mad.gifttrack.ui.screens.auth.AuthViewModel
import uk.ac.tees.mad.gifttrack.ui.theme.GiftTrackTheme

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
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                val showBottomBar = currentRoute in listOf(
                    Routes.GIFT_LIST.route,
                    Routes.CALENDAR.route,
                    Routes.PROFILE.route
                )


                Scaffold(bottomBar = {
                    if(showBottomBar) {
                        BottomNavBar(navController)
                    }
                }, modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(navController, authViewModel)
                }
            }
        }
    }
}
