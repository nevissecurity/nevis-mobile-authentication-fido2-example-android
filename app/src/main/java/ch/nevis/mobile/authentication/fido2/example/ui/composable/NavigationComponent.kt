package ch.nevis.mobile.authentication.fido2.example.ui.composable

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ch.nevis.mobile.authentication.fido2.example.ui.screen.home.HomeScreen
import kotlinx.serialization.Serializable

/**
 * Navigation destination object for the home screen route.
 *
 * Used as the type-safe route in [NavHost] via Kotlinx Serialization.
 */
@Serializable
object Home

/**
 * Root Compose navigation component that wires destinations to composable screens.
 *
 * @param navController The [NavHostController] managing back-stack and navigation.
 * @param onLaunchCustomTab Callback forwarded to [HomeScreen] for web-based authorization.
 */
@Composable
fun NavigationComponent(
    navController: NavHostController,
    onLaunchCustomTab: (Uri) -> Unit
) {
    NavHost(navController = navController, startDestination = Home) {
        composable<Home>() {
            HomeScreen(
                onLaunchCustomTab = onLaunchCustomTab
            )
        }
    }
}
