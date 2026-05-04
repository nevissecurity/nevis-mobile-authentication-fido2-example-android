package ch.nevis.mobile.authentication.fido2.example.domain.model

import ch.nevis.mobile.authentication.fido2.example.data.model.CredentialRequestOptions

/**
 * Domain model representing the server's response to a start-approval request.
 *
 * @property credentialRequestOptions The WebAuthn `PublicKeyCredentialRequestOptions` challenge
 *           to be forwarded to Android Credential Manager.
 * @property statusToken Opaque token that must be included in [CompleteApprovalRequest].
 */
data class StartApprovalResponse(
    val credentialRequestOptions: CredentialRequestOptions,
    val statusToken: String,
)
