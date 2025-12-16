package uk.ac.tees.mad.gifttrack.ui.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.gifttrack.domain.repository.GiftRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val giftRepository: GiftRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun handleGoogleSignInResult(data: Intent?) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)

            Log.d("AuthViewModel", "Google account obtained: ${account.email}")

            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) { // FIX: Was checking 'task' before
                    Log.d("AuthViewModel", "Firebase sign-in successful: ${auth.currentUser?.email}")
                    saveUserProfileToFirestore()
                    _isLoggedIn.value = true
                    syncUserData()
                } else {
                    _isLoggedIn.value = false
                    Log.e("AuthViewModel", "Firebase sign-in failed: ${authTask.exception?.message}")
                }
            }
        } catch (e: ApiException) {
            Log.e("AuthViewModel", "Google Sign-In failed with code: ${e.statusCode}")
            when (e.statusCode) {
                10 -> Log.e("AuthViewModel", "DEVELOPER_ERROR: Check SHA-1, package name, and Web Client ID")
                12501 -> Log.e("AuthViewModel", "SIGN_IN_CANCELLED: User cancelled sign-in")
                7 -> Log.e("AuthViewModel", "NETWORK_ERROR: No internet connection")
                else -> Log.e("AuthViewModel", "Error: ${e.message}")
            }
            _isLoggedIn.value = false
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Unexpected error: ${e.message}", e)
            _isLoggedIn.value = false
        }
    }

    fun syncUserData() {
        viewModelScope.launch {
            try {
                Log.d("AuthViewModel", "Syncing Firestore → Room...")
                giftRepository.syncFromFirestore()
                Log.d("AuthViewModel", "Sync completed.")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Sync failed: ${e.message}", e)
            }
        }
    }

    private fun saveUserProfileToFirestore() {
        val user = auth.currentUser ?: return
        val uid = user.uid

        val profile = mapOf(
            "name" to (user.displayName ?: ""),
            "email" to (user.email ?: ""),
            "photoUrl" to (user.photoUrl?.toString() ?: ""),
            "bio" to "",
            "themeMode" to false,
            "notifications" to true
        )

        firestore.collection("users")
            .document(uid)
            .set(profile, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("AuthViewModel", "User profile saved to Firestore")
            }
            .addOnFailureListener { e ->
                Log.e("AuthViewModel", "Failed to save user profile: ${e.message}")
            }
    }

    fun signOut() {
        auth.signOut()
        _isLoggedIn.value = false
    }
}