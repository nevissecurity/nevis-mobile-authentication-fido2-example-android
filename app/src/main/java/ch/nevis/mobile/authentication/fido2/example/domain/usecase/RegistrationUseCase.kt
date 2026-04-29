package ch.nevis.mobile.authentication.fido2.example.domain.usecase

import android.content.Context
import ch.nevis.mobile.authentication.fido2.example.domain.model.RegistrationOptions

/**
 * Use case that drives the two-phase FIDO2 passkey registration ceremony.
 *
 * Phase 1: Fetches a [CredentialCreationOptions] challenge from the server.
 * Phase 2: After the user creates a passkey via Android Credential Manager,
 *          submits the attestation response back to the server.
 */
interface RegistrationUseCase {

    /**
     * Executes the full registration ceremony.
     *
     * @param context An activity-based [Context] required by [CredentialManager.createCredential]
     *                to display the system passkey creation UI.
     * @param username The username to enroll.
     * @param registrationOptions FIDO2 options such as user-verification requirement,
     *                            authenticator attachment, attestation preference, and resident-key setting.
     * @throws androidx.credentials.exceptions.CreateCredentialException if the user cancels
     *         the passkey creation UI or no suitable authenticator is available.
     * @throws retrofit2.HttpException if the server returns a non-2xx response.
     */
    suspend fun execute(
        context: Context,
        username: String,
        registrationOptions: RegistrationOptions,
    )
}
