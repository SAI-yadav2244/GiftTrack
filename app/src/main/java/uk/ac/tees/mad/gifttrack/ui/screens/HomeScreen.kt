package uk.ac.tees.mad.gifttrack.ui.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        HomeNavHost(navController)
    }
}

@Composable
fun HomeNavHost(x0: NavHostController) {
    TODO("Not yet implemented")
}

@Composable
fun BottomNavigationBar(x0: NavHostController) {
    TODO("Not yet implemented")
}
