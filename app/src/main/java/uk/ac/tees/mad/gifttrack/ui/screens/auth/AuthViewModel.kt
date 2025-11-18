package uk.ac.tees.mad.gifttrack.ui.screens.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
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
                } else {
                    _isLoggedIn.value = false
                    println("Sign-in failed: ${task.exception?.message}")
                }            }
        } catch (e: Exception) {
            e.printStackTrace()
            _isLoggedIn.value = false
        }

    }

    fun signOut() {
        auth.signOut()
        _isLoggedIn.value = false
    }
}