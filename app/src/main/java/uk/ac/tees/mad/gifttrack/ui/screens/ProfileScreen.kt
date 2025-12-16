package uk.ac.tees.mad.gifttrack.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uk.ac.tees.mad.gifttrack.ui.components.ProfileActionItem
import uk.ac.tees.mad.gifttrack.ui.components.ProfileSettingItem
import uk.ac.tees.mad.gifttrack.ui.viewmodel.ProfileViewModel
import java.nio.file.WatchEvent

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel
) {
    val name = viewModel.name.value
    val email = viewModel.email.value
    val photo = viewModel.photoUrl.value
    val bio = viewModel.bio.value

    val isDarkMode = viewModel.themeMode.value

    val context = LocalContext.current

    var showEditBioDialog by remember { mutableStateOf(false) }
    var editedBio by remember { mutableStateOf(bio) }
    LaunchedEffect(Unit) {
        viewModel.refreshProfile()  // This ensures fresh data every time you visit
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Profile",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Avatar
        AsyncImage(
            model = photo,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))
        Text(name, style = MaterialTheme.typography.titleMedium)
        Text(email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

        Spacer(Modifier.height(24.dp))

        // Bio section
        Text(
            text = if (bio.isNotEmpty()) bio else "Add a short bio...",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .clickable { showEditBioDialog = true }
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(24.dp))

        Divider()

        // Theme toggle
        ProfileSettingItem(
            icon = Icons.Default.Menu,
            title = "Dark Theme",
            checked = isDarkMode,
            onCheckedChange = { viewModel.saveTheme(it) }
        )

        // Connect to Gmail
        ProfileActionItem(
            icon = Icons.Default.Email,
            title = "Connect Gmail",
            onClick = { viewModel.connectGmail(context) }
        )

        // Connect to Contacts
        ProfileActionItem(
            icon = Icons.Default.Phone,
            title = "Import from Contacts",
            onClick = { viewModel.importFromContacts(context) }
        )

        Spacer(Modifier.height(40.dp))

        // Logout
        Button(
            onClick = {
                viewModel.logout()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout",)
        }
    }

    // Edit Bio Dialog
    if (showEditBioDialog) {
        AlertDialog(
            onDismissRequest = { showEditBioDialog = false },
            title = { Text("Edit Bio") },
            text = {
                OutlinedTextField(
                    value = editedBio,
                    onValueChange = { editedBio = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Your bio") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.saveBio(editedBio)
                    showEditBioDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditBioDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

