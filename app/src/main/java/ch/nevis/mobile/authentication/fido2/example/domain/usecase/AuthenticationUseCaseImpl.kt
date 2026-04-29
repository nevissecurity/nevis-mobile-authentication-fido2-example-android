package ch.nevis.mobile.authentication.fido2.example.domain.usecase

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PublicKeyCredential
import ch.nevis.mobile.authentication.fido2.example.credentials.model.PublicKeyCredentialAuthenticationResponse
import ch.nevis.mobile.authentication.fido2.example.domain.model.AuthenticationOptions
import ch.nevis.mobile.authentication.fido2.example.domain.model.CompleteApprovalRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.StartApprovalRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.StartApprovalResponse
import ch.nevis.mobile.authentication.fido2.example.domain.repository.Fido2Repository
import com.google.gson.Gson

/**
 * Default implementation of [AuthenticationUseCase] that orchestrates the two-phase
 * FIDO2 passkey authentication ceremony using Android Credential Manager and the
 * Nevis Authentication Cloud backend.
 */
class AuthenticationUseCaseImpl(
    private val credentialManager: CredentialManager,
    private val fido2Repository: Fido2Repository,
    private val gson: Gson,
) : AuthenticationUseCase {

    companion object {
        private val TAG = AuthenticationUseCaseImpl::class.java.name
    }

    /**
     * Executes the two-phase authentication ceremony.
     *
     * Phase 1: Calls [Fido2Repository.startApproval] to obtain a [CredentialRequestOptions]
     * challenge from the server.
     * Phase 2: Invokes [getPasskey] to present the system passkey selector, then submits
     * the assertion to the server via [Fido2Repository.completeApproval].
     *
     * @param context Activity-based context for the Credential Manager UI.
     * @param username The username for account-bound flow, or `null` for discoverable-credential flow.
     * @param authenticationOptions FIDO2 options for the ceremony.
     */
    override suspend fun execute(
        context: Context,
        username: String?,
        authenticationOptions: AuthenticationOptions
    ) {
        // If username is null, the server will initiate a discoverable-credential (usernameless) flow.
        val startApprovalResponse =
            fido2Repository.startApproval(StartApprovalRequest(username, authenticationOptions))
        getPasskey(context, startApprovalResponse)
    }

    private suspend fun getPasskey(context: Context, startApprovalResponse: StartApprovalResponse) {
        val getPublicKeyCredentialOption =
            GetPublicKeyCredentialOption(requestJson = gson.toJson(startApprovalResponse.credentialRequestOptions))
        val getCredentialRequest = GetCredentialRequest(
            listOf(getPublicKeyCredentialOption)
        )

        val getCredentialResponse = credentialManager.getCredential(
            context = context,
            request = getCredentialRequest
        )
        handleGetCredentialResponse(getCredentialResponse, startApprovalResponse)
    }

    private suspend fun handleGetCredentialResponse(
        getCredentialResponse: GetCredentialResponse,
        startApprovalResponse: StartApprovalResponse,
    ) {
        val credential = getCredentialResponse.credential
        if (credential is PublicKeyCredential) {
            Log.d(
                TAG,
                "AuthenticationResponseJson: ${credential.authenticationResponseJson}"
            )
            val publicKeyCredentialAuthenticationResponse =
                gson.fromJson<PublicKeyCredentialAuthenticationResponse>(
                    credential.authenticationResponseJson,
                    PublicKeyCredentialAuthenticationResponse::class.java
                )

            fido2Repository.completeApproval(
                CompleteApprovalRequest(
                    publicKeyCredentialAuthenticationResponse,
                    startApprovalResponse.statusToken,
                )
            )
        }
    }
}
