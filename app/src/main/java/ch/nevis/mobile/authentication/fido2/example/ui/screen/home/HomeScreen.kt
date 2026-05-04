package ch.nevis.mobile.authentication.fido2.example.ui.screen.home

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.core.util.Consumer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import ch.nevis.mobile.authentication.fido2.example.BuildConfig
import ch.nevis.mobile.authentication.fido2.example.R
import ch.nevis.mobile.authentication.fido2.example.ui.composable.HomeScreenContent
import ch.nevis.mobile.authentication.fido2.example.ui.composable.LoadingIndicator
import ch.nevis.mobile.authentication.fido2.example.ui.composable.TopAppBar

/**
 * Root composable for the home screen.
 *
 * Observes [HomeScreenViewModel] state, registers lifecycle and new-intent listeners to handle
 * OAuth deep-link callbacks from Chrome Custom Tabs, and renders the [Scaffold] with
 * [TopAppBar], [HomeScreenContent], and [LoadingIndicator].
 *
 * @param viewModel The [HomeScreenViewModel] instance (injected by Hilt via [hiltViewModel]).
 * @param lifecycleOwner The lifecycle owner used to attach observers.
 * @param onLaunchCustomTab Callback invoked when the user requests a web-based operation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onLaunchCustomTab: (Uri) -> Unit,
) {
    val localContext = LocalContext.current
    val activity = LocalActivity.current as ComponentActivity
    val username by viewModel.username.collectAsState()
    val authenticationOptions by viewModel.authenticationOptions.collectAsState()
    val registrationOptions by viewModel.registrationOptions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val resultMessage by viewModel.resultMessage.collectAsState()

    DisposableEffect(lifecycleOwner) {
        // As MainActivity is configured with android:launchMode="singleTask",
        // this observer is not necessary, but without "singleTask" flag
        // onCreate is called and this observer as well.
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    viewModel.processIntent(activity.intent)
                    activity.intent?.data = null
                }

                else -> {
                    // Do nothing.
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // As MainActivity is configured with android:launchMode="singleTask",
        // onNewIntent and this listener is called. We can handle the Intent
        // here that comes from Custom Tab here.
        val onNewIntentListener = object : Consumer<Intent> {
            override fun accept(intent: Intent) {
                viewModel.processIntent(intent)
                intent.data = null
            }
        }
        activity.addOnNewIntentListener(onNewIntentListener)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            activity.removeOnNewIntentListener(onNewIntentListener)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar()
        }
    ) { innerPadding ->
        val authenticationOperation = stringResource(R.string.operation_authentication)
        val registrationOperation = stringResource(R.string.operation_registration)
        val token = BuildConfig.AccessToken
        val scheme = stringResource(R.string.auth_redirect_scheme)

        HomeScreenContent(
            modifier = Modifier.padding(innerPadding),
            currentUsername = username,
            currentAuthenticationOptions = authenticationOptions,
            currentRegistrationOptions = registrationOptions,
            resultMessage = resultMessage,
            host = stringResource(
                R.string.main_host,
                BuildConfig.BaseUrl.toUri().host ?: stringResource(R.string.main_host_unknown)
            ),
            onUsernameChange = { viewModel.updateUsername(it) },
            onRegisterButtonClicked = { viewModel.register(context = localContext) },
            onAuthenticateButtonClicked = { viewModel.authenticate(context = localContext) },
            onAuthenticateUsernamelessButtonClicked = { viewModel.authenticateUsernameless(context = localContext) },
            onRegisterViaWebButtonClicked = {
                onLaunchCustomTab("${BuildConfig.BaseUrl}/fido2-test.html?token=$token&operation=$registrationOperation&redirectTo=$scheme".toUri())
            },
            onAuthenticateViaWebButtonClicked = {
                onLaunchCustomTab("${BuildConfig.BaseUrl}/fido2-test.html?token=$token&operation=$authenticationOperation&redirectTo=$scheme".toUri())
            },
            onAuthenticationOptionsChange = { viewModel.updateAuthenticationOptions(it) },
            onRegistrationOptionsChange = { viewModel.updateRegistrationOptions(it) },
        )
    }

    if (isLoading) {
        LoadingIndicator(modifier = Modifier.fillMaxSize())
    }
}
