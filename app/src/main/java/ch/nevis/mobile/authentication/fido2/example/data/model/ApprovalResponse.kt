package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO for the `POST api/v1/approval/` response body.
 *
 * @property transactionId Unique identifier of this authentication transaction.
 * @property userId The server-assigned user identifier.
 * @property statusToken Opaque token to include in the subsequent assertion request.
 * @property credentialRequestOptions The WebAuthn `PublicKeyCredentialRequestOptions` challenge.
 */
data class ApprovalResponse(
    val transactionId: String,
    val userId: String,
    val statusToken: String,
    val credentialRequestOptions: CredentialRequestOptions,
)
