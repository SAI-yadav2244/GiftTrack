package uk.ac.tees.mad.gifttrack.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    private val _user = mutableStateOf(auth.currentUser)
    val user get() = _user.value

    var name = mutableStateOf(user?.displayName ?: "")
        private set
    private val _themeMode = MutableStateFlow(false)
    val themeMode: StateFlow<Boolean> = _themeMode
    var email = mutableStateOf(user?.email ?: "")
        private set
    var photoUrl = mutableStateOf(user?.photoUrl?.toString())
        private set
    var bio = mutableStateOf("")
        private set
    var notificationsEnabled = mutableStateOf(true)
        private set

    // Add a loading state if you want (optional)
    var isLoading = mutableStateOf(false)
        private set

    init {
        // Listen to auth state changes
        auth.addAuthStateListener { firebaseAuth ->
            val currentUser = firebaseAuth.currentUser
            _user.value = currentUser
            updateFirebaseUserFields(currentUser)
            if (currentUser != null) {
                loadUserProfile(currentUser.uid)
            } else {
                clearProfileData()
            }
        }

        // Initial load in case user is already signed in
        auth.currentUser?.let { loadUserProfile(it.uid) }
    }

    private fun updateFirebaseUserFields(firebaseUser: com.google.firebase.auth.FirebaseUser?) {
        name.value = firebaseUser?.displayName ?: ""
        email.value = firebaseUser?.email ?: ""
        photoUrl.value = firebaseUser?.photoUrl?.toString()
    }

    private fun clearProfileData() {
        bio.value = ""
        _themeMode.value = false
        notificationsEnabled.value = true
    }

    private fun loadUserProfile(uid: String) {
        isLoading.value = true
        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    bio.value = doc.getString("bio") ?: ""
                    _themeMode.value = doc.getBoolean("themeMode") ?: false
                    notificationsEnabled.value = doc.getBoolean("notifications") ?: true
                }
                isLoading.value = false
            }
            .addOnFailureListener {
                it.printStackTrace()
                isLoading.value = false
            }
    }

    // Public method to manually refresh (useful after login or profile changes)
    fun refreshProfile() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateFirebaseUserFields(currentUser)
            loadUserProfile(currentUser.uid)
        }
    }
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
        _themeMode.value = enabled

        viewModelScope.launch {
            firestore.collection("users")
                .document(uid)
                .update("themeMode", enabled)
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    _themeMode.value = !enabled
                }
        }
    }

    fun saveNotifications(enabled: Boolean) {
        val uid = user?.uid ?: return
        notificationsEnabled.value = enabled
        firestore.collection("users")
            .document(uid)
            .update("notifications", enabled)
    }

    fun saveBio(newBio: String) {
        val uid = user?.uid ?: return

        viewModelScope.launch {
            firestore.collection("users")
                .document(uid)
                .update("bio", newBio)
                .addOnSuccessListener {
                    bio.value = newBio
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
        }
    }

    fun connectGmail(context: Context) {
        Toast.makeText(context, "Connecting to Gmail...", Toast.LENGTH_SHORT).show()
    }

    fun importFromContacts(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Unable to open Contacts", Toast.LENGTH_SHORT).show()
        }
    }

    fun logout() {
        auth.signOut()
    }
}
