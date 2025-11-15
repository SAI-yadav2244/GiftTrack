package uk.ac.tees.mad.gifttrack.ui.screens.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(

) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
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
                _isLoggedIn.value = auth.currentUser != null
            }
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