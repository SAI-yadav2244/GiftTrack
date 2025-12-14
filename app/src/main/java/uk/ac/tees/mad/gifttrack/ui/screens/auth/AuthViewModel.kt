package uk.ac.tees.mad.gifttrack.ui.screens.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.gifttrack.domain.repository.GiftRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val giftRepository: GiftRepository
) : ViewModel() {
//    private val auth = FirebaseAuth.getInstance()
    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun handleGoogleSignInResult(data: Intent?) {
        try {

            val task = GoogleSignIn.getSignedInAccountFromIntent(
                data
            )
            val account = task.result ?: return
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener {
                if (task.isSuccessful) {
                    _isLoggedIn.value = true
                    println("Sign-in successful: ${auth.currentUser?.email}")
                    syncUserData()
                } else {
                    _isLoggedIn.value = false
                    println("Sign-in failed: ${task.exception?.message}")
                }            }
        } catch (e: Exception) {
            e.printStackTrace()
            _isLoggedIn.value = false
        }
    }

    private fun syncUserData() {
        viewModelScope.launch {
            try {
                println("Syncing Firestore → Room...")
                giftRepository.syncFromFirestore()
                println("Sync completed.")
            } catch (e: Exception) {
                println("Sync failed: ${e.message}")
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _isLoggedIn.value = false
    }
}