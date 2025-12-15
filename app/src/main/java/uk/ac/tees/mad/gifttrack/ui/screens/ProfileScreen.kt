package uk.ac.tees.mad.gifttrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uk.ac.tees.mad.gifttrack.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel
) {
    val name = viewModel.name.value
    val email = viewModel.email.value
    val photo = viewModel.photoUrl.value
    val context = LocalContext.current

    Column(
        Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Profile",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Avatar
        AsyncImage(
            model = photo,
            contentDescription = "Profile Picture",
            modifier = Modifier.size(100.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(name, style = MaterialTheme.typography.headlineSmall)
        Text(email, style = MaterialTheme.typography.bodyMedium)

//        Spacer(Modifier.height(24.dp))

//        // Example toggles
//        Text("Theme")
//        Switch(
//            checked = viewModel.themeMode.value,
//            onCheckedChange = { viewModel.saveTheme(it) }
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        Text("Notifications")
//        Switch(
//            checked = viewModel.notificationsEnabled.value,
//            onCheckedChange = { viewModel.saveNotifications(it) }
//        )

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = {
                viewModel.logout()
                onLogout()
            }
        ) {
            Text("Logout")
        }
    }
}
