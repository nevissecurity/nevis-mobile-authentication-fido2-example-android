package ch.nevis.mobile.authentication.fido2.example.domain.usecase

import android.content.Context
import ch.nevis.mobile.authentication.fido2.example.domain.model.AuthenticationOptions

/**
 * Use case that drives the two-phase FIDO2 passkey authentication ceremony.
 *
 * Supports both account-bound (username-based) and discoverable-credential
 * (usernameless) authentication flows.
 */
interface AuthenticationUseCase {

    /**
     * Executes the full authentication ceremony.
     *
     * @param context An activity-based [Context] required by [CredentialManager.getCredential]
     *                to display the system passkey selection UI.
     * @param username The username to authenticate. Pass `null` to initiate a
     *                 discoverable-credential (usernameless) flow where the user selects
     *                 their passkey from the system-presented list.
     * @param authenticationOptions FIDO2 options such as the user-verification requirement.
     * @throws androidx.credentials.exceptions.GetCredentialException if the user cancels
     *         the passkey selection UI or no matching credential is available.
     * @throws retrofit2.HttpException if the server returns a non-2xx response.
     */
    suspend fun execute(
        context: Context,
        username: String? = null,
        authenticationOptions: AuthenticationOptions
    )
}
