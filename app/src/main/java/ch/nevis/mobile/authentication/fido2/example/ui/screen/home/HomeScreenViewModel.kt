package ch.nevis.mobile.authentication.fido2.example.ui.screen.home

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.nevis.mobile.authentication.fido2.example.domain.model.AuthenticationOptions
import ch.nevis.mobile.authentication.fido2.example.domain.model.IntrospectRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.RegistrationOptions
import ch.nevis.mobile.authentication.fido2.example.domain.usecase.AuthenticationUseCase
import ch.nevis.mobile.authentication.fido2.example.domain.usecase.IntrospectUseCase
import ch.nevis.mobile.authentication.fido2.example.domain.usecase.RegistrationUseCase
import ch.nevis.mobile.authentication.fido2.example.ui.model.EmptyResultMessage
import ch.nevis.mobile.authentication.fido2.example.ui.model.ErrorResultMessage
import ch.nevis.mobile.authentication.fido2.example.ui.model.FailureWebAuthorizationResult
import ch.nevis.mobile.authentication.fido2.example.ui.model.ResultMessage
import ch.nevis.mobile.authentication.fido2.example.ui.model.SuccessResultMessage
import ch.nevis.mobile.authentication.fido2.example.ui.model.SuccessWebAuthorizationResult
import ch.nevis.mobile.authentication.fido2.example.ui.model.WebAuthorizationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

/**
 * ViewModel for [HomeScreen], exposing the UI state via [StateFlow] and handling
 * all FIDO2 operations.
 *
 * Coordinates [RegistrationUseCase], [AuthenticationUseCase], and [IntrospectUseCase]
 * with coroutines launched in [viewModelScope].
 */
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    private val introspectUseCase: IntrospectUseCase,
) : ViewModel() {

    companion object {
        private val TAG = HomeScreenViewModel::class.java.name
    }

    private val _username = MutableStateFlow("")

    /** The current username entered by the user. */
    val username = _username.asStateFlow()

    private val _resultMessage = MutableStateFlow<ResultMessage>(EmptyResultMessage())

    /** The current result or error message to display. */
    val resultMessage = _resultMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    /** Whether a background operation is currently in progress. */
    val isLoading = _isLoading.asStateFlow()

    private val _registrationOptions = MutableStateFlow(RegistrationOptions())

    /** The current FIDO2 registration options selected by the user. */
    val registrationOptions = _registrationOptions.asStateFlow()

    private val _authenticationOptions = MutableStateFlow(AuthenticationOptions())

    /** The current FIDO2 authentication options selected by the user. */
    val authenticationOptions = _authenticationOptions.asStateFlow()

    /**
     * Initiates passkey registration for the current [username] using [registrationOptions].
     *
     * @param context An activity-based context required by Android Credential Manager.
     */
    fun register(context: Context) {
        setState(isLoading = true)
        viewModelScope.launch {
            try {
                registrationUseCase.execute(context, _username.value, _registrationOptions.value)
                setState(SuccessResultMessage(message = "Successful registration."))
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e // ensure co-operative cancellation
                }
                handleFailure(e)
            }
        }
    }

    /**
     * Updates the current [username] state.
     *
     * @param username The new username value.
     */
    fun updateUsername(username: String) {
        _username.value = username
    }

    /**
     * Updates the current [authenticationOptions] state.
     *
     * @param authenticationOptions The new authentication options.
     */
    fun updateAuthenticationOptions(authenticationOptions: AuthenticationOptions) {
        _authenticationOptions.value = authenticationOptions
    }

    /**
     * Updates the current [registrationOptions] state.
     *
     * @param registrationOptions The new registration options.
     */
    fun updateRegistrationOptions(registrationOptions: RegistrationOptions) {
        _registrationOptions.value = registrationOptions
    }

    /**
     * Initiates passkey authentication for the current [username] using [authenticationOptions].
     *
     * @param context An activity-based context required by Android Credential Manager.
     */
    fun authenticate(context: Context) {
        authenticate(context, username.value)
    }

    /**
     * Initiates a usernameless (discoverable-credential) passkey authentication.
     *
     * No username is provided to the server; the user selects their passkey from the
     * system UI presented by Android Credential Manager.
     *
     * @param context An activity-based context required by Android Credential Manager.
     */
    fun authenticateUsernameless(context: Context) {
        authenticate(context, null)
    }

    /**
     * Handles OAuth deep-link callbacks received from Chrome Custom Tabs.
     *
     * Parses the [intent] data URI host to determine success or failure, then processes
     * the result accordingly (validates the token on success, shows the error on failure).
     *
     * @param intent The [Intent] delivered by the deep-link, or `null` if not applicable.
     */
    fun processIntent(intent: Intent?) {
        return when (intent?.data?.host) {
            "success" -> {
                val token = intent.data?.getQueryParameter("token")
                    ?: throw IllegalStateException("Token parameter was not received.")
                processWebAuthorizationResult(SuccessWebAuthorizationResult(token))
            }

            "failure" -> {
                val error = intent.data?.getQueryParameter("error")
                    ?: throw IllegalStateException("Error parameter was not received.")
                processWebAuthorizationResult(FailureWebAuthorizationResult(error))
            }

            else -> {
                // Do nothing.
            }
        }
    }

    private fun authenticate(context: Context, username: String?) {
        setState(isLoading = true)
        viewModelScope.launch {
            try {
                authenticationUseCase.execute(context, username, _authenticationOptions.value)
                setState(SuccessResultMessage(message = "Successful authentication."))
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e // ensure co-operative cancellation
                }
                handleFailure(e)
            }
        }
    }

    private fun validateToken(token: String) {
        setState(isLoading = true)
        viewModelScope.launch {
            try {
                val introspectResponse = introspectUseCase.execute(IntrospectRequest(token))
                val state = if (introspectResponse.isActive) {
                    SuccessResultMessage(
                        title = "Token is valid",
                        message = introspectResponse.toString(),
                    )
                } else {
                    ErrorResultMessage(
                        title = "Token is not valid",
                        message = introspectResponse.toString(),
                    )
                }
                setState(state)
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e // ensure co-operative cancellation
                }
                handleFailure(e)
            }
        }
    }

    private fun handleFailure(e: Exception) {
        val errorMessage = when (e) {
            is HttpException -> {
                e.response()?.errorBody()?.string()
            }

            else -> e.message
        } ?: e.message

        setState(ErrorResultMessage(message = errorMessage.toString()))
        Log.e(TAG, errorMessage, e)
    }

    private fun setState(
        resultMessage: ResultMessage = EmptyResultMessage(),
        isLoading: Boolean = false
    ) {
        _resultMessage.value = resultMessage
        _isLoading.value = isLoading
    }

    private fun processWebAuthorizationResult(webAuthorizationResult: WebAuthorizationResult? = null) {
        when (webAuthorizationResult) {
            is FailureWebAuthorizationResult -> setState(
                ErrorResultMessage(message = webAuthorizationResult.error)
            )

            is SuccessWebAuthorizationResult -> {
                validateToken(webAuthorizationResult.token)
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
