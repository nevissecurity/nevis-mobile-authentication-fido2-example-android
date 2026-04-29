package ch.nevis.mobile.authentication.fido2.example.domain.usecase

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.credentials.CreateCredentialResponse
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.CredentialManager
import ch.nevis.mobile.authentication.fido2.example.credentials.model.PublicKeyCredentialRegistrationResponse
import ch.nevis.mobile.authentication.fido2.example.data.util.UserFriendlyNameProvider
import ch.nevis.mobile.authentication.fido2.example.domain.model.CompleteRegistrationRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.RegistrationOptions
import ch.nevis.mobile.authentication.fido2.example.domain.model.StartRegistrationRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.StartRegistrationResponse
import ch.nevis.mobile.authentication.fido2.example.domain.repository.Fido2Repository
import com.google.gson.Gson

/**
 * Default implementation of [RegistrationUseCase] that orchestrates the two-phase
 * FIDO2 passkey registration ceremony using Android Credential Manager and the
 * Nevis Authentication Cloud backend.
 */
class RegistrationUseCaseImpl(
    private val credentialManager: CredentialManager,
    private val fido2Repository: Fido2Repository,
    private val gson: Gson,
    private val userFriendlyNameProvider: UserFriendlyNameProvider,
) : RegistrationUseCase {

    companion object {
        private val TAG = RegistrationUseCaseImpl::class.java.name
    }

    /**
     * Executes the two-phase registration ceremony.
     *
     * Phase 1: Calls [Fido2Repository.startRegistration] to obtain a
     * [CredentialCreationOptions] challenge from the server.
     * Phase 2: Invokes [createPasskey] to let the user create a passkey and then
     * submits the attestation to the server via [Fido2Repository.completeRegistration].
     */
    override suspend fun execute(
        context: Context,
        username: String,
        registrationOptions: RegistrationOptions,
    ) {
        // Phase 1: Request CredentialCreationOptions challenge from the server.
        val startRegistrationResponse = fido2Repository.startRegistration(
            StartRegistrationRequest(
                username,
                registrationOptions
            )
        )
        // Phase 2: Prompt the user to create a passkey and submit the attestation to the server.
        createPasskey(context, startRegistrationResponse)
    }

    /**
     * Builds a [CreatePublicKeyCredentialRequest] from [startRegistrationResponse] and
     * triggers the system passkey creation UI via [CredentialManager].
     *
     * @param context Activity-based context for the Credential Manager UI.
     * @param startRegistrationResponse Server response containing the WebAuthn challenge.
     * @param preferImmediatelyAvailableCredentials When `true`, only on-device authenticators
     *        are considered (no hybrid/QR-code flow). Defaults to `true` to streamline the UX
     *        for this example app and avoid presenting out-of-band options to the user.
     */
    @SuppressLint("PublicKeyCredential")
    private suspend fun createPasskey(
        context: Context,
        startRegistrationResponse: StartRegistrationResponse,
        preferImmediatelyAvailableCredentials: Boolean = true
    ) {
        val createPublicKeyCredentialRequest = CreatePublicKeyCredentialRequest(
            // Contains the request in JSON format. Uses the standard WebAuthn
            // web JSON spec.
            requestJson = gson.toJson(startRegistrationResponse.credentialCreationOptions),
            // Defines whether you prefer to use only immediately available
            // credentials, not hybrid credentials, to fulfill this request.
            // This value is false by default.
            preferImmediatelyAvailableCredentials = preferImmediatelyAvailableCredentials,
        )
        val createCredentialResponse = credentialManager.createCredential(
            // Use an activity-based context to avoid undefined system
            // UI launching behavior
            context = context,
            request = createPublicKeyCredentialRequest,
        )
        handleCreateCredentialResponse(createCredentialResponse, startRegistrationResponse)
    }

    /**
     * Handles the [CreateCredentialResponse] returned by Android Credential Manager.
     *
     * Deserializes the registration JSON into [PublicKeyCredentialRegistrationResponse] and
     * submits the attestation to [Fido2Repository.completeRegistration].
     *
     * @param createCredentialResponse The response from [CredentialManager.createCredential].
     * @param startRegistrationResponse The original server challenge response, used to obtain
     *        the username and status token.
     * @throws IllegalArgumentException if the response is not a [CreatePublicKeyCredentialResponse].
     */
    private suspend fun handleCreateCredentialResponse(
        createCredentialResponse: CreateCredentialResponse,
        startRegistrationResponse: StartRegistrationResponse
    ) {
        if (createCredentialResponse is CreatePublicKeyCredentialResponse) {
            Log.d(
                TAG,
                "RegistrationResponseJson: ${createCredentialResponse.registrationResponseJson}"
            )
            val publicKeyCredentialRegistrationResponse =
                gson.fromJson<PublicKeyCredentialRegistrationResponse>(
                    createCredentialResponse.registrationResponseJson,
                    PublicKeyCredentialRegistrationResponse::class.java
                )


            fido2Repository.completeRegistration(
                CompleteRegistrationRequest(
                    username = startRegistrationResponse.username,
                    statusToken = startRegistrationResponse.statusToken,
                    userFriendlyName = userFriendlyNameProvider.get(),
                    publicKeyCredentialRegistrationResponse = publicKeyCredentialRegistrationResponse,
                )
            )
        } else {
            throw IllegalArgumentException("Unexpected CreateCredentialResponse instance received. Class: ${createCredentialResponse::class.java}")
        }
    }
}
