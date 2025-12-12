package uk.ac.tees.mad.gifttrack.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
//    private val dataStore: UserPreferencesDataStore
) : ViewModel() {

    val user = auth.currentUser

    // UI state
    var name = mutableStateOf(user?.displayName ?: "")
        private set

    var email = mutableStateOf(user?.email ?: "")
        private set

    var photoUrl = mutableStateOf(user?.photoUrl?.toString())
        private set

    var bio = mutableStateOf("")
        private set

    var themeMode = mutableStateOf(false)
        private set

    var notificationsEnabled = mutableStateOf(true)
        private set


    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val uid = user?.uid ?: return

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    bio.value = doc.getString("bio") ?: ""
                    themeMode.value = doc.getBoolean("themeMode") ?: false
                    notificationsEnabled.value = doc.getBoolean("notifications") ?: true
                }
            }
    }

    // Example preference values
//    val themeMode = dataStore.themeFlow.collectAsState(initial = false)
//    val notificationsEnabled = dataStore.notificationFlow.collectAsState(initial = true)

    fun updateBio(newBio: String) {
        val uid = user?.uid ?: return

        firestore.collection("users")
            .document(uid)
            .update("bio", newBio)

        bio.value = newBio
    }


//    fun saveNotifications(enabled: Boolean) {
//        viewModelScope.launch {
//            dataStore.saveNotifications(enabled)
//        }
//    }


    fun saveTheme(enabled: Boolean) {
        val uid = user?.uid ?: return
        themeMode.value = enabled
        firestore.collection("users")
            .document(uid)
            .update("themeMode", enabled)
    }

    fun saveNotifications(enabled: Boolean) {
        val uid = user?.uid ?: return
        notificationsEnabled.value = enabled
        firestore.collection("users")
            .document(uid)
            .update("notifications", enabled)
    }

    fun logout() {
        auth.signOut()
    }
}
