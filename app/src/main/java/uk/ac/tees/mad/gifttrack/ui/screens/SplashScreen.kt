package uk.ac.tees.mad.gifttrack.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import uk.ac.tees.mad.gifttrack.R


@Composable
fun SplashScreen(
    navigateToAuth: () -> Unit,
    navigateToHome: () -> Unit
) {

    val auth = FirebaseAuth.getInstance()
    val loggedIn = auth.currentUser != null

    val alphaAnim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(1200))
        delay(1800)
        if (loggedIn) navigateToHome() else navigateToAuth()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_gift),
            contentDescription = "GiftTrack Logo",
            modifier = Modifier
                .size(160.dp)
                .alpha(alphaAnim.value)
        )
    }
}