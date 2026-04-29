package ch.nevis.mobile.authentication.fido2.example

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.compose.rememberNavController
import ch.nevis.mobile.authentication.fido2.example.ui.composable.NavigationComponent
import ch.nevis.mobile.authentication.fido2.example.ui.theme.FIDO2ExampleTheme
import dagger.hilt.android.AndroidEntryPoint


/**
 * The single [ComponentActivity] of the app.
 *
 * Configured with `android:launchMode="singleTask"` so that deep-link intents from
 * Chrome Custom Tabs are delivered via [onNewIntent] rather than creating a new instance.
 * Sets up the Compose content with [FIDO2ExampleTheme] and [NavigationComponent].
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FIDO2ExampleTheme {
                NavigationComponent(
                    navController = rememberNavController(),
                    onLaunchCustomTab = { openCustomTab(it) },
                )
            }
        }
    }

    private fun openCustomTab(uri: Uri) {
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(this, uri)
    }
}
