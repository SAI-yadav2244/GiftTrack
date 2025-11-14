package uk.ac.tees.mad.gifttrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Welcome to GiftTrack",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = {
            if(auth.currentUser != null) {
                onLoginSuccess()
            }
        },
            shape = RoundedCornerShape(10.dp),
            elevation = ButtonDefaults.buttonElevation(6.dp),
            ) {
            Text("Continue with Google")
        }
//        Button(
//            onClick = { viewModel.doLogin() },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = !uiState.loading,
//            shape = RoundedCornerShape(10.dp),
//            elevation = ButtonDefaults.buttonElevation(6.dp),
//        ) {
//            Text(if (uiState.loading) "Logging in..." else "Login")
//        }

    }
}