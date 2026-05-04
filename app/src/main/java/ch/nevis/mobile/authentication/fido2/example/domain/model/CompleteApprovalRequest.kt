package ch.nevis.mobile.authentication.fido2.example.domain.model

import ch.nevis.mobile.authentication.fido2.example.credentials.model.PublicKeyCredentialAuthenticationResponse

/**
 * Domain model representing the input for the second phase of the passkey authentication ceremony.
 *
 * @property publicKeyCredentialAuthenticationResponse The assertion response returned by Android
 *           Credential Manager after the user selects and uses a passkey.
 * @property statusToken The opaque token returned by [StartApprovalResponse].
 */
data class CompleteApprovalRequest(
    val publicKeyCredentialAuthenticationResponse: PublicKeyCredentialAuthenticationResponse,
    val statusToken: String,
)
